package one.microproject.scheduler.service;

import java.util.Optional;

public interface ProviderFactoryService {

    Optional<JobProvider> get(String taskType);

}
