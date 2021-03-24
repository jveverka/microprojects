package one.microproject.scheduler.service;

import one.microproject.scheduler.dto.TaskInfo;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ProviderFactoryServiceImpl implements ProviderFactoryService {

    private final Map<String, JobProvider> providers;

    public ProviderFactoryServiceImpl(Map<String, JobProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Flux<TaskInfo> getTaskInfo() {
        Stream<TaskInfo> taskInfoStream = providers.values().stream().map(JobProvider::getTaskInfo);
        return Flux.fromStream(taskInfoStream);
    }

    @Override
    public Optional<JobProvider<?,?>> get(String taskType) {
        return Optional.ofNullable(providers.get(taskType));
    }

}
