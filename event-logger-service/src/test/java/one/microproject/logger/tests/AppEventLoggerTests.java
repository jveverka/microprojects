package one.microproject.logger.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.logger.dto.CreateDataSeriesRequest;
import one.microproject.logger.dto.DataSeriesInfo;
import one.microproject.logger.dto.GenericResponse;
import one.microproject.logger.dto.InsertDataRecord;
import one.microproject.logger.model.DataRecord;
import one.microproject.logger.tests.testdto.DataWrapper;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = AppEventLoggerTests.Initializer.class)
public class AppEventLoggerTests {

    private static final int DOCKER_EXPOSED_MONGO_PORT = 27017;
    private static final String MONGO_DOCKER_IMAGE = "mongo:4.2.9";
    private static MongoDBContainer mongoDBContainer;
    private static String accessToken = "access_token";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebTestClient webClient;

    @Test
    @Order(1)
    public void testInitialServiceState() {
        EntityExchangeResult<DataSeriesInfo[]> entityExchangeResult = getDataSeriesInfo();
        assertEquals(0, entityExchangeResult.getResponseBody().length);
    }

    @Test
    @Order(2)
    public void testCreateDataSeries() {
        CreateDataSeriesRequest createDataSeriesRequest = new CreateDataSeriesRequest("g001", "s001", "d");
        EntityExchangeResult<GenericResponse> entityExchangeResult = webClient.post().uri("/services/series")
                .bodyValue(createDataSeriesRequest)
                .header("Authorization", "Bearer: " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GenericResponse.class).returnResult();
        assertNotNull(entityExchangeResult.getResponseBody());
        assertEquals(Boolean.TRUE, entityExchangeResult.getResponseBody().getOk());

        EntityExchangeResult<DataSeriesInfo[]> entityExchangeResultDataSeriesInfo = getDataSeriesInfo();
        assertEquals(1, entityExchangeResultDataSeriesInfo.getResponseBody().length);
    }

    @Test
    @Order(3)
    public void testGetDataSeries() {
        EntityExchangeResult<DataSeriesInfo> entityExchangeResult = webClient.get().uri("/services/series/{groupId}/{name}", "g001", "s001")
                .header("Authorization", "Bearer: " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DataSeriesInfo.class).returnResult();
        assertNotNull(entityExchangeResult.getResponseBody());
        assertEquals("g001", entityExchangeResult.getResponseBody().getGroupId());
        assertEquals("s001", entityExchangeResult.getResponseBody().getName());
    }

    @Test
    @Order(4)
    public void testInsertDataRecords() {
        DataWrapper dataWrapper = new DataWrapper(120, true, "example", DataWrapper.getInstance());
        JsonNode jsonNode = mapper.valueToTree(dataWrapper);
        InsertDataRecord insertDataRecord = new InsertDataRecord(1609459190L, "g001", "s001", jsonNode);
        EntityExchangeResult<GenericResponse> entityExchangeResult = webClient.put().uri("/services/records")
                .bodyValue(insertDataRecord)
                .header("Authorization", "Bearer: " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GenericResponse.class).returnResult();
        assertNotNull(entityExchangeResult.getResponseBody());
        assertEquals(Boolean.TRUE, entityExchangeResult.getResponseBody().getOk());
    }

    @Test
    @Order(5)
    public void testGetDataRecords() {
        EntityExchangeResult<DataRecord[]> entityExchangeResult = webClient.get().uri("/services/records/{groupId}/{name}", "g001", "s001")
                .header("Authorization", "Bearer: " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DataRecord[].class).returnResult();

        assertNotNull(entityExchangeResult.getResponseBody());
        assertEquals(1, entityExchangeResult.getResponseBody().length);
    }

    @Test
    @Order(80)
    public void testDeleteDataSeries() {
        EntityExchangeResult<GenericResponse> entityExchangeResult = webClient.delete().uri("/services/series/{groupId}/{name}", "g001", "s001")
                .header("Authorization", "Bearer: " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(GenericResponse.class).returnResult();

        EntityExchangeResult<DataSeriesInfo[]> entityExchangeResultDataSeriesInfo = getDataSeriesInfo();
        assertEquals(0, entityExchangeResultDataSeriesInfo.getResponseBody().length);
    }

    private EntityExchangeResult<DataSeriesInfo[]> getDataSeriesInfo() {
        return webClient.get().uri("/services/series")
                .header("Authorization", "Bearer: " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DataSeriesInfo[].class).returnResult();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private final Logger LOGGER = LoggerFactory.getLogger(AppEventLoggerTests.class);

        private static MongoDBContainer mongoDBContainer = new MongoDBContainer(MONGO_DOCKER_IMAGE);

        @Override
        public void initialize(@NonNull ConfigurableApplicationContext context) {

            mongoDBContainer.start();
            Assertions.assertTrue(mongoDBContainer.isRunning());
            AppEventLoggerTests.mongoDBContainer = mongoDBContainer;
            List<Integer> exposedPorts = mongoDBContainer.getExposedPorts();
            Integer port = exposedPorts.get(0);
            Integer boundPort = mongoDBContainer.getMappedPort(port);

            LOGGER.info("MONGO     : {}", mongoDBContainer.getReplicaSetUrl());
            LOGGER.info("MONGO     : mongodb://localhost:{}/test", boundPort);

            TestPropertyValues.of(
                    //"spring.data.mongodb.uri=" + mongoDBContainer.getReplicaSetUrl()
                    "spring.data.mongodb.host=localhost",
                    "spring.data.mongodb.port=" + boundPort,
                    "spring.data.mongodb.database=test"
            ).applyTo(context.getEnvironment());
        }
    }

}
