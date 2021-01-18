package one.microproject.scheduler.service;

import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.ScheduleJobRequest;
import one.microproject.scheduler.dto.ScheduledJobInfo;
import one.microproject.scheduler.dto.TaskInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PeriodicSchedulerService {

    Flux<TaskInfo> getTypes();

    Mono<JobId> schedule(ScheduleJobRequest request);

    Flux<ScheduledJobInfo> getScheduledJobs();

    Mono<JobId> cancel(JobId jobId);

}
