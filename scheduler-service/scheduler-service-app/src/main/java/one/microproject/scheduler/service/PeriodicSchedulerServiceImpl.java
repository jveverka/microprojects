package one.microproject.scheduler.service;

import com.fasterxml.jackson.databind.JsonNode;
import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.JobWrapper;
import one.microproject.scheduler.dto.ScheduleJobRequest;
import one.microproject.scheduler.dto.ScheduledJobInfo;
import one.microproject.scheduler.dto.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class PeriodicSchedulerServiceImpl implements PeriodicSchedulerService, JobResultCache {

    private static final Logger LOG = LoggerFactory.getLogger(PeriodicSchedulerServiceImpl.class);

    private final ScheduledExecutorService executorService;
    private final ProviderFactoryService providerFactoryService;
    private final Map<JobId, JobWrapper> jobs;

    @Autowired
    public PeriodicSchedulerServiceImpl(ProviderFactoryService providerFactoryService) {
        this.executorService = Executors.newScheduledThreadPool(8);
        this.providerFactoryService = providerFactoryService;
        this.jobs = new ConcurrentHashMap<>();
    }

    @Override
    public Flux<TaskInfo> getTypes() {
        return providerFactoryService.getTaskInfo();
    }

    @Override
    public Mono<JobId> schedule(ScheduleJobRequest request) {
        Optional<JobProvider> provider = providerFactoryService.get(request.getTaskType());
        JobId id = JobId.from(UUID.randomUUID().toString());
        try {
            if (provider.isPresent()) {
                Runnable job = provider.get().createJob(id, request.getTaskParameters(), this);
                ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(job, 0L, request.getInterval(), request.getTimeUnit());
                JobWrapper wrapper = new JobWrapper(scheduledFuture);
                jobs.put(id, wrapper);
                return Mono.just(id);
            }
        } catch (CreateJobException e) {
            LOG.warn("Failed to create Job {}", id.getId());
        }
        return Mono.empty();
    }

    @Override
    public Flux<ScheduledJobInfo> getScheduledJobs() {
        return null;
    }

    @Override
    public Mono<JobId> cancel(JobId jobId) {
        LOG.info("cancel {}", jobId);
        JobWrapper jobWrapper = jobs.get(jobId);
        if (jobWrapper != null) {
            jobWrapper.getScheduledFuture().cancel(true);
            return Mono.just(jobId);
        } else {
            return Mono.empty();
        }
    }

    @PostConstruct
    private void shutdown() throws InterruptedException {
        LOG.info("shutdown ...");
        executorService.shutdown();
        while(!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            LOG.info("waiting for executor ...");
        }
    }

    @Override
    public void setResult(JobId jobId, JsonNode result) {
        LOG.info("setResult {}", jobId.getId());
    }

}
