package one.microproject.scheduler.service;

import one.microproject.scheduler.dto.TaskInfo;
import reactor.core.publisher.Flux;

import java.util.Optional;

public interface ProviderFactoryService {

    Flux<TaskInfo> getTaskInfo();

    Optional<JobProvider> get(String taskType);

}
