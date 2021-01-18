package one.microproject.scheduler.config;

import one.microproject.scheduler.service.JobProvider;
import one.microproject.scheduler.service.ProviderFactoryService;
import one.microproject.scheduler.service.ProviderFactoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@ConfigurationProperties(prefix = "app")
public class ProviderConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ProviderConfig.class);

    private List<String> providers;
    private ProviderFactoryService providerFactoryService;

    @PostConstruct
    public void init() {
        LOG.info("Providers: {}", providers.size());
        Map<String, JobProvider> providerMap = new ConcurrentHashMap<>();
        for (String provider: providers) {
            try {
                LOG.info("  Loading class: {}", provider);
                Class<? extends JobProvider> clazz = Class.forName(provider).asSubclass(JobProvider.class);
                LOG.info("  Creating provider instance ...");
                JobProvider jobProvider = clazz.getDeclaredConstructor().newInstance();
                LOG.info("  Registering provider for taskType={}", jobProvider.getTaskType());
                providerMap.put(jobProvider.getTaskType(), jobProvider);
            } catch (Exception e) {
                LOG.error("Error: ", e);
            }
        }
        this.providerFactoryService = new ProviderFactoryServiceImpl(providerMap);
    }

    @Bean
    public ProviderFactoryService getProviderFactoryService() {
        return providerFactoryService;
    }

    public List<String> getProviders() {
        return providers;
    }

    public void setProviders(List<String> providers) {
        this.providers = providers;
    }

}
