package one.microproject.scheduler.service;

import java.util.Map;
import java.util.Optional;

public class ProviderFactoryServiceImpl implements ProviderFactoryService {

    private final Map<String, JobProvider> providers;

    public ProviderFactoryServiceImpl(Map<String, JobProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Optional<JobProvider> get(String taskType) {
        return Optional.ofNullable(providers.get(taskType));
    }

}
