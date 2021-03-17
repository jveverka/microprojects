package one.microproject.scheduler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.JobResultData;
import one.microproject.scheduler.dto.JobResultInfo;
import one.microproject.scheduler.dto.JobStatus;
import one.microproject.scheduler.dto.JobWrapper;
import one.microproject.scheduler.dto.ScheduleJobRequest;
import one.microproject.scheduler.dto.ScheduledJobInfo;
import one.microproject.scheduler.dto.TaskInfo;
import one.microproject.scheduler.repository.ScheduledJobRepository;
import one.microproject.scheduler.model.ScheduledJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
public class PeriodicSchedulerServiceImpl implements PeriodicSchedulerService, JobResultCache {

    private static final Logger LOG = LoggerFactory.getLogger(PeriodicSchedulerServiceImpl.class);

    private final ScheduledExecutorService executorService;
    private final ProviderFactoryService providerFactoryService;
    private final Map<JobId, JobWrapper> jobs;
    private final ScheduledJobRepository scheduledJobRepository;
    private final ObjectMapper mapper;
    private final ResultService resultService;

    @Autowired
    public PeriodicSchedulerServiceImpl(ScheduledJobRepository scheduledJobRepository,
                                        ProviderFactoryService providerFactoryService,
                                        ResultService resultService) {
        this.executorService = Executors.newScheduledThreadPool(8);
        this.providerFactoryService = providerFactoryService;
        this.scheduledJobRepository = scheduledJobRepository;
        this.jobs = new ConcurrentHashMap<>();
        this.mapper = new ObjectMapper();
        this.resultService = resultService;
    }

    @PostConstruct
    public void init() {
        LOG.info("init ...");
        scheduledJobRepository.findAll().toStream().forEach(s -> {
            LOG.info("  job init {}", s.getId());
            try {
                JobId id = JobId.from(s.getId());
                JsonNode jsonNode = mapper.readTree(s.getTaskParameters());
                Optional<JobProvider> provider = providerFactoryService.get(s.getTaskType());
                if (provider.isPresent()) {
                    LOG.info("  job init schedule {}", s.getTaskType());
                    //TODO: check the start date
                    Runnable job = provider.get().createJob(id, jsonNode, this);
                    ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(job, 1L, s.getInterval(), s.getTimeUnit());
                    JobWrapper wrapper = new JobWrapper(scheduledFuture, id);
                    jobs.put(id, wrapper);
                } else {
                    LOG.info("  job init ERROR: no TaskType={} provided", s.getTaskType());
                }
            } catch (JsonProcessingException | CreateJobException e) {
                LOG.warn("Failed to create Job {}", s.getId());
            }
        });
    }

    @Override
    public Flux<TaskInfo> getTypes() {
        return providerFactoryService.getTaskInfo();
    }

    @Override
    @Transactional
    public Mono<JobId> schedule(ScheduleJobRequest request) {
        JobId id = JobId.from(UUID.randomUUID().toString());
        try {
            Optional<JobProvider> provider = providerFactoryService.get(request.getTaskType());
            if (provider.isPresent()) {
                LOG.info("schedule {}", request.getTaskType());
                //TODO: check the start date
                Runnable job = provider.get().createJob(id, request.getTaskParameters(), this);
                ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(job, 1L, request.getInterval(), request.getTimeUnit());
                JobWrapper wrapper = new JobWrapper(scheduledFuture, id);
                String parameters = mapper.writeValueAsString(request.getTaskParameters());
                ScheduledJob scheduledJob = new ScheduledJob(id.getId(), request.getTaskType(),
                        request.getStartDate(), provider.get().getTaskInfo().getName(),
                        request.getInterval(), request.getTimeUnit(), request.getRepeat(), 0L,  parameters);
                jobs.put(id, wrapper);
                Mono<ScheduledJob> saved = scheduledJobRepository.save(scheduledJob);
                return saved.transform(mono -> mono.map( m -> JobId.from(m.getId())));
            }
        } catch (JsonProcessingException | CreateJobException e) {
            LOG.warn("Failed to create Job {}", id.getId());
        }
        return Mono.empty();
    }

    @Override
    public Flux<ScheduledJobInfo> getScheduledJobs() {
        return scheduledJobRepository.findAll().transform(flux -> flux.map(this::transform));
    }

    @Override
    @Transactional
    public Mono<JobId> cancel(JobId jobId) {
        LOG.info("cancel {}", jobId);
        Mono<Void> deleted = scheduledJobRepository.deleteById(jobId.getId());
        JobWrapper jobWrapper = jobs.remove(jobId);
        if (jobWrapper != null) {
            jobWrapper.getScheduledFuture().cancel(true);
        }
        return deleted.transform(mono -> mono.map( m -> jobId));
    }

    @Override
    @Transactional
    public void setResult(JobId jobId, Long startedTimeStamp, Long duration, JsonNode result) {
        LOG.info("setResult {}", jobId.getId());
        Mono<ScheduledJob> jobMono = scheduledJobRepository.findById(jobId.getId());
        jobMono.subscribe(s -> {
            resultService.save(transformToJobResultInfo(s, startedTimeStamp, duration, JobStatus.RUNNING, result)).subscribe();
            s.setCounter(s.getCounter() + 1);
            LOG.info("updating counter {}/{}", jobId.getId(), s.getCounter());
            Mono<ScheduledJob> save = scheduledJobRepository.save(s);
            save.subscribe(c -> LOG.info("updated counter {}/{}", jobId.getId(), c.getCounter()));
            if (s.getRepeat() > 0 && s.getCounter() >= s.getRepeat()) {
                cancel(jobId).subscribe();
            }
        });
    }

    @PreDestroy
    private void shutdown() throws InterruptedException {
        LOG.info("shutdown ...");
        executorService.shutdown();
        while(!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            LOG.info("waiting for executor ...");
        }
    }

    private ScheduledJobInfo transform(ScheduledJob scheduledJob) {
        LOG.info("transform: {}", scheduledJob.getId());
        JobId id = JobId.from(scheduledJob.getId());
        JobWrapper wrapper = jobs.get(id);
        if (wrapper !=  null) {
            return new ScheduledJobInfo(id, scheduledJob.getTaskType(), scheduledJob.getStartDate(), scheduledJob.getName(),
                    scheduledJob.getInterval(), scheduledJob.getRepeat(), scheduledJob.getCounter(),
                    scheduledJob.getTimeUnit(), null /* TODO transform(id)*/);
        } else {
            return new ScheduledJobInfo(id, scheduledJob.getTaskType(), scheduledJob.getStartDate(), scheduledJob.getName(),
                    scheduledJob.getInterval(), scheduledJob.getRepeat(), scheduledJob.getCounter(),
                    scheduledJob.getTimeUnit(), null);
        }
    }

    /*
    private JobResultData transform(JobResultInfo jobResultInfo) {
        return new JobResultData(jobResultInfo.getStartedTimeStamp(), jobResultInfo.getDuration(), jobResultInfo.getResult());
    }

    private JobResultData transform(JobId id) {
        try {
            CompletableFuture<JobResultData> future = new CompletableFuture<>();
            resultService.get(id).subscribe(j -> future.complete(transform(j)) );
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }
    */

    private JobResultInfo transformToJobResultInfo(ScheduledJob scheduledJob, Long startedTimeStamp,
                                                   Long duration, JobStatus status, JsonNode result) {
        return new JobResultInfo(JobId.from(scheduledJob.getId()), scheduledJob.getTaskType(), startedTimeStamp, duration, status, result);
    }

}
