package one.microproject.scheduler.service;

import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.ScheduleTaskRequest;
import one.microproject.scheduler.dto.ScheduledTaskInfo;
import one.microproject.scheduler.dto.TaskInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PeriodicSchedulerService {

    Flux<TaskInfo> getTypes();

    Mono<JobId> schedule(ScheduleTaskRequest request);

    Flux<ScheduledTaskInfo> getScheduledTasks();

    void cancel(JobId jobId);

}
