package one.microproject.scheduler.service;

import com.fasterxml.jackson.databind.JsonNode;
import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.JobWrapper;
import one.microproject.scheduler.dto.ScheduleTaskRequest;
import one.microproject.scheduler.dto.ScheduledTaskInfo;
import one.microproject.scheduler.dto.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Map;
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
    private final Map<String, JobProvider> providers;
    private final Map<JobId, JobWrapper> jobs;

    public PeriodicSchedulerServiceImpl() {
        this.executorService = Executors.newScheduledThreadPool(8);
        this.providers = new ConcurrentHashMap<>();
        this.jobs = new ConcurrentHashMap<>();
    }

    @Override
    public Flux<TaskInfo> getTypes() {
        return null;
    }

    @Override
    public Mono<JobId> schedule(ScheduleTaskRequest request) {
        JobProvider provider = providers.get(request.getTaskType());
        if (provider != null) {
            JobId id = JobId.from(UUID.randomUUID().toString());
            Runnable job = provider.createJob(id, request.getTaskParameters(), this);
            ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(job, 0L, request.getInterval(), request.getTimeUnit());
            JobWrapper wrapper = new JobWrapper(scheduledFuture);
            jobs.put(id, wrapper);
            return Mono.just(id);
        } else {
            return Mono.empty();
        }
    }

    @Override
    public Flux<ScheduledTaskInfo> getScheduledTasks() {
        return null;
    }

    @Override
    public void cancel(JobId jobId) {
        LOG.info("cancel {}", jobId);
        JobWrapper jobWrapper = jobs.get(jobId);
        if (jobWrapper != null) {
            jobWrapper.getScheduledFuture().cancel(true);
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

    }

}
