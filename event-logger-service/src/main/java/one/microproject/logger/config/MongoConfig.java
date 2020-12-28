package one.microproject.logger.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ConfigurationProperties(prefix="spring.data.mongodb")
public class MongoConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MongoConfig.class);

    private String database;

    @PostConstruct
    public void init() {
        LOG.info("MONGO: DB={}",  database);
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

}
