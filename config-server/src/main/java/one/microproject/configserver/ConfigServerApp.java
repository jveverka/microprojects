package one.microproject.configserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ConfigurableApplicationContext;

@EnableConfigServer
@SpringBootApplication
public class ConfigServerApp {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigServerApp.class);

    public static void main(String[] args) {
        LOG.info("Spring Config-Server started ...");
        ConfigurableApplicationContext run = SpringApplication.run(ConfigServerApp.class, args);
        run.registerShutdownHook();
    }

}
