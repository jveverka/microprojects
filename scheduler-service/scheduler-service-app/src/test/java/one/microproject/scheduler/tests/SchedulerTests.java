package one.microproject.scheduler.tests;

import one.microproject.scheduler.dto.TaskInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SchedulerTests {

    private static final int DOCKER_EXPOSED_MONGO_PORT = 27017;
    private static final String MONGO_DOCKER_IMAGE = "mongo:4.2.9";

    @Autowired
    private WebTestClient webClient;

    private static String accessToken = "";

    @Test
    @Order(0)
    public void testGetTypes() {
        EntityExchangeResult<TaskInfo[]> taskInfosExchange = webClient.get().uri("/services/tasks/types")
                .header("Authorization", "Bearer: " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskInfo[].class).returnResult();
        TaskInfo[] taskInfos = taskInfosExchange.getResponseBody();
        assertEquals(1, taskInfos.length);
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private final Logger LOGGER = LoggerFactory.getLogger(SchedulerTests.class);

        private static MongoDBContainer mongoDBContainer = new MongoDBContainer(MONGO_DOCKER_IMAGE);
        //private static GenericContainer<?> iamServiceContainer = new GenericContainer<>(IAM_SERVICE_DOCKER_IMAGE)
        //        .withExposedPorts(IAM_SERVICE_EXPOSED_PORT)
        //        .withReuse(true);

        @Override
        public void initialize(@NonNull ConfigurableApplicationContext context) {

            mongoDBContainer.start();
            Assertions.assertTrue(mongoDBContainer.isRunning());
            //SchedulerTests.mongoDBContainer = mongoDBContainer;
            Integer boundPort = mongoDBContainer.getMappedPort(DOCKER_EXPOSED_MONGO_PORT);

            LOGGER.info("MONGO      : {}", mongoDBContainer.getReplicaSetUrl());
            LOGGER.info("MONGO      : mongodb://localhost:{}/test", boundPort);

            //iamServiceContainer.start();
            //Assertions.assertTrue(iamServiceContainer.isRunning());

            //String iamServiceBaseURL = "http://" + iamServiceContainer.getContainerIpAddress() + ":" + iamServiceContainer.getMappedPort(IAM_SERVICE_EXPOSED_PORT);
            //LOGGER.info("IAM-SERVICE: {}:{}", iamServiceContainer.getContainerIpAddress(), iamServiceContainer.getMappedPort(IAM_SERVICE_EXPOSED_PORT));
            //LOGGER.info("IAM-SERVICE: {}", iamServiceBaseURL);
            //SchedulerTests.iamServiceBaseURL = iamServiceBaseURL;

            TestPropertyValues.of(
                    "spring.data.mongodb.host=localhost",
                    "spring.data.mongodb.port=" + boundPort,
                    "spring.data.mongodb.database=test"
            //        "app.iamClient.baseUrl=" + iamServiceBaseURL
            ).applyTo(context.getEnvironment());
        }
    }

}
