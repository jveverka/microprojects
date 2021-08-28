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

    private String host;
    private String port;
    private String database;
    private String user;
    private String password;

    @PostConstruct
    public void init() {
        LOG.info("## MONGO: Host={}",  host);
        LOG.info("## MONGO: Port={}",  port);
        LOG.info("## MONGO: DB={}",  database);
        LOG.info("## MONGO: user={}",  user);
        LOG.info("## MONGO: password != null: {}",  password != null);
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
