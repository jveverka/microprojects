package one.microproject.scheduler.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.JobResultData;
import one.microproject.scheduler.dto.JobResultInfo;
import one.microproject.scheduler.dto.JobStatus;
import one.microproject.scheduler.dto.JobWrapper;
import one.microproject.scheduler.dto.ResultCode;
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
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
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
                Optional<JobProvider<?,?>> provider = providerFactoryService.get(s.getTaskType());
                if (provider.isPresent()) {
                    LOG.info("  job init schedule {}", s.getTaskType());
                    //TODO: check the start date
                    JobWrapper wrapper = createAndScheduleJob(id, provider.get(), s.getInterval(), s.getTimeUnit(), jsonNode);
                    jobs.put(id, wrapper);
                } else {
                    LOG.info("  job init ERROR: no TaskType={} provided", s.getTaskType());
                }
            } catch (IOException | CreateJobException e) {
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
            Optional<JobProvider<?,?>> provider = providerFactoryService.get(request.getTaskType());
            if (provider.isPresent()) {
                LOG.info("schedule {}", request.getTaskType());
                //TODO: check the start date
                JobWrapper wrapper = createAndScheduleJob(id, provider.get(), request.getInterval(), request.getTimeUnit(), request.getTaskParameters());
                String parameters = mapper.writeValueAsString(request.getTaskParameters());
                ScheduledJob scheduledJob = new ScheduledJob(id.getId(), request.getTaskType(),
                        request.getStartDate(), provider.get().getTaskInfo().getName(),
                        request.getInterval(), request.getTimeUnit(), request.getRepeat(), 0L,  parameters);
                jobs.put(id, wrapper);
                Mono<ScheduledJob> saved = scheduledJobRepository.save(scheduledJob);
                return saved.transform(mono -> mono.map( m -> JobId.from(m.getId())));
            }
        } catch (IOException | CreateJobException e) {
            LOG.warn("Failed to create Job {}", id.getId());
        }
        return Mono.empty();
    }

    @Override
    public Flux<ScheduledJobInfo> getScheduledJobs() {
        return scheduledJobRepository.findAll().transform(f -> f.concatMap(this::transformScheduledJob));
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
        resultService.statusUpdate(jobId, JobStatus.TERMINATED).subscribe();
        return deleted.transform(mono -> mono.map( m -> jobId));
    }

    @Override
    @Transactional
    public void setResult(JobId jobId, Long startedTimeStamp, Long duration, ResultCode code, JsonNode result) {
        LOG.info("setResult {}", jobId.getId());
        Mono<ScheduledJob> jobMono = scheduledJobRepository.findById(jobId.getId());
        jobMono.subscribe(s -> {
            resultService.save(transformToJobResultInfo(s, startedTimeStamp, duration, JobStatus.RUNNING, code, result)).subscribe();
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

    private JobWrapper createAndScheduleJob(JobId id, JobProvider jobProvider, Long interval, TimeUnit timeUnit, JsonNode jsonNode) throws CreateJobException, IOException {
        Object taskParameters = mapper.readValue(mapper.treeAsTokens(jsonNode), jobProvider.getTaskParametersType());
        JobInstance jobInstance = jobProvider.createJob(id, taskParameters);
        JobHandler handler = new JobHandler(id, jobInstance, this);
        ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(handler, 1L, interval, timeUnit);
        return new JobWrapper(scheduledFuture, id);
    }

    private Mono<ScheduledJobInfo> transformScheduledJob(ScheduledJob scheduledJob) {
        LOG.info("transform: {}", scheduledJob.getId());
        JobId id = JobId.from(scheduledJob.getId());
        return  resultService.get(id).map(m -> {
            JobWrapper wrapper = jobs.get(id);
            if (wrapper !=  null) {
                return new ScheduledJobInfo(id, scheduledJob.getTaskType(), scheduledJob.getStartDate(), scheduledJob.getName(),
                        scheduledJob.getInterval(), scheduledJob.getRepeat(), scheduledJob.getCounter(),
                        scheduledJob.getTimeUnit(), transform(m));
            } else {
                return new ScheduledJobInfo(id, scheduledJob.getTaskType(), scheduledJob.getStartDate(), scheduledJob.getName(),
                        scheduledJob.getInterval(), scheduledJob.getRepeat(), scheduledJob.getCounter(),
                        scheduledJob.getTimeUnit(), null);
            }
        });
    }

    private JobResultData transform(JobResultInfo jobResultInfo) {
        return new JobResultData(jobResultInfo.getStartedTimeStamp(), jobResultInfo.getDuration(), jobResultInfo.getResult());
    }

    private JobResultInfo transformToJobResultInfo(ScheduledJob scheduledJob, Long startedTimeStamp,
                                                   Long duration, JobStatus status, ResultCode code, JsonNode result) {
        return new JobResultInfo(JobId.from(scheduledJob.getId()), scheduledJob.getTaskType(), startedTimeStamp, duration, status, code, result);
    }

}
