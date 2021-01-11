package one.microproject.scheduler.service;

import one.microproject.scheduler.dto.ScheduleTaskRequest;
import one.microproject.scheduler.dto.TaskInfo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SchedulerServiceImpl implements SchedulerService {

    @Override
    public void scheduleTask(ScheduleTaskRequest request) {

    }

    @Override
    public Flux<TaskInfo> getTasks() {
        return null;
    }

    @Override
    public void cancelTask(String taskId) {

    }

}
