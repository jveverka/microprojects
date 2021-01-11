package one.microproject.scheduler.service;

import one.microproject.scheduler.dto.ScheduleTaskRequest;
import one.microproject.scheduler.dto.TaskInfo;
import reactor.core.publisher.Flux;

public interface SchedulerService {

    void scheduleTask(ScheduleTaskRequest request);

    Flux<TaskInfo> getTasks();

    void cancelTask(String taskId);

}
