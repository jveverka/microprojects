package one.microproject.logger.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.iamservice.client.IAMClient;
import one.microproject.iamservice.core.dto.CreateClient;
import one.microproject.iamservice.core.dto.CreateRole;
import one.microproject.iamservice.core.dto.CreateUser;
import one.microproject.iamservice.core.dto.PermissionInfo;
import one.microproject.iamservice.core.dto.StandardTokenClaims;
import one.microproject.iamservice.core.dto.TokenResponse;
import one.microproject.iamservice.core.dto.TokenResponseWrapper;
import one.microproject.iamservice.core.model.ClientId;
import one.microproject.iamservice.core.model.ClientProperties;
import one.microproject.iamservice.core.model.JWToken;
import one.microproject.iamservice.core.model.RoleId;
import one.microproject.iamservice.core.model.UserId;
import one.microproject.iamservice.core.model.UserProperties;
import one.microproject.iamservice.core.services.dto.SetupOrganizationRequest;
import one.microproject.iamservice.core.services.dto.SetupOrganizationResponse;
import one.microproject.iamservice.core.utils.ModelUtils;
import one.microproject.iamservice.serviceclient.IAMAuthorizerClient;
import one.microproject.iamservice.serviceclient.IAMServiceClientBuilder;
import one.microproject.iamservice.serviceclient.IAMServiceManagerClient;
import one.microproject.iamservice.serviceclient.IAMServiceProjectManagerClient;
import one.microproject.iamservice.serviceclient.IAMServiceUserManagerClient;
import one.microproject.iamservice.serviceclient.impl.AuthenticationException;
import one.microproject.logger.config.IAMClientConfiguration;
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
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = AppEventLoggerTests.Initializer.class)
public class AppEventLoggerTests {

    private static final Logger LOG = LoggerFactory.getLogger(AppEventLoggerTests.class);

    private static final int DOCKER_EXPOSED_MONGO_PORT = 27017;
    private static final int IAM_SERVICE_EXPOSED_PORT = 8080;
    private static final String MONGO_DOCKER_IMAGE = "mongo:4.4.4";
    private static final String IAM_SERVICE_DOCKER_IMAGE = "jurajveverka/iam-service:2.5.5-RELEASE-amd64";
    private static MongoDBContainer mongoDBContainer;
    private static String iamServiceBaseURL;
    private static String accessToken = "access_token";

    private static IAMServiceManagerClient iamServiceManagerClient;
    private static IAMServiceProjectManagerClient iamServiceProjectClient;
    private static IAMServiceUserManagerClient iamServiceUserManagerClient;
    private static IAMAuthorizerClient iamAuthorizerClient;

    private static TokenResponse globalAdminTokens;
    private static TokenResponse projectAdminTokens;
    private static TokenResponse readUserTokens;
    private static TokenResponse writeUserTokens;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private IAMClient iamClient;

    @Autowired
    private IAMClientConfiguration configuration;

    @Test
    @Order(0)
    public void setupIAMService() throws IOException {
        URL baseUrl = new URL(iamServiceBaseURL);
        iamServiceManagerClient = IAMServiceClientBuilder.builder()
                .withBaseUrl(baseUrl)
                .withConnectionTimeout(60L, TimeUnit.SECONDS)
                .build();
        TokenResponseWrapper tokenResponseWrapper = iamServiceManagerClient
                .getIAMAdminAuthorizerClient()
                .getAccessTokensOAuth2UsernamePassword("admin", "secret", ModelUtils.IAM_ADMIN_CLIENT_ID, "top-secret");
        assertTrue(tokenResponseWrapper.isOk());
        globalAdminTokens = tokenResponseWrapper.getTokenResponse();
    }

    @Test
    @Order(1)
    public void createProjectAndProjectAdmin() throws AuthenticationException {
        SetupOrganizationRequest request = new SetupOrganizationRequest(configuration.getOrganizationId().getId(), "",
                configuration.getProjectId().getId(), "", "admin-client", "acs",
                "admin", "7s4sa5", "", Set.of(configuration.getProjectId().getId()), "", new UserProperties(Map.of()));
        SetupOrganizationResponse setupOrganizationResponse = iamServiceManagerClient.setupOrganization(globalAdminTokens.getAccessToken(), request);
        assertNotNull(setupOrganizationResponse);
    }

    @Test
    @Order(2)
    public void createProjectUsers() throws IOException, AuthenticationException {
        iamAuthorizerClient = iamServiceManagerClient.getIAMAuthorizerClient(configuration.getOrganizationId(), configuration.getProjectId());
        TokenResponseWrapper tokenResponseWrapper = iamAuthorizerClient.getAccessTokensOAuth2UsernamePassword("admin", "7s4sa5", ClientId.from("admin-client"), "acs");
        assertTrue(tokenResponseWrapper.isOk());
        projectAdminTokens = tokenResponseWrapper.getTokenResponse();

        iamServiceProjectClient = iamServiceManagerClient.getIAMServiceProject(projectAdminTokens.getAccessToken(), configuration.getOrganizationId(), configuration.getProjectId());
        CreateClient createClient = new CreateClient("client-001", "", 3600L, 3600L, "ds65f", ClientProperties.from(""));
        iamServiceProjectClient.createClient(createClient);
        iamServiceUserManagerClient = iamServiceManagerClient.getIAMServiceUserManagerClient(projectAdminTokens.getAccessToken(), configuration.getOrganizationId(), configuration.getProjectId());
        CreateUser createReadOnlyUser = new CreateUser("read-user", "", 3600L, 3600L, "", "as87d6a", new UserProperties(Map.of()));
        CreateUser createReadWriteUser = new CreateUser("write-user", "", 3600L, 3600L, "", "6a57dfa", new UserProperties(Map.of()));
        iamServiceUserManagerClient.createUser(createReadOnlyUser);
        iamServiceUserManagerClient.createUser(createReadWriteUser);
    }

    @Test
    @Order(3)
    public void setProjectUserRolesAndPermissions() throws AuthenticationException {
        CreateRole createReaderRole = new CreateRole("reader-role", "", Set.of(
                new PermissionInfo(configuration.getProjectId().getId(), "data-series-all", "read")
        ));
        iamServiceProjectClient.createRole(createReaderRole);
        CreateRole createWriterRole = new CreateRole("writer-role", "", Set.of(
                new PermissionInfo(configuration.getProjectId().getId(), "data-series-all", "all")
        ));
        iamServiceProjectClient.createRole(createWriterRole);
        CreateRole createAdminRole = new CreateRole("admin-role", "", Set.of(
                new PermissionInfo(configuration.getProjectId().getId(), "all", "all")
        ));
        iamServiceProjectClient.createRole(createAdminRole);

        iamServiceUserManagerClient.addRoleToUser(UserId.from("read-user"), RoleId.from("reader-role"));
        iamServiceUserManagerClient.addRoleToUser(UserId.from("write-user"), RoleId.from("writer-role"));
        iamServiceUserManagerClient.addRoleToUser(UserId.from("admin"), RoleId.from("admin-role"));
    }

    @Test
    @Order(4)
    public void getUserTokens() throws IOException {
        TokenResponseWrapper readUserWrapper = iamAuthorizerClient.getAccessTokensOAuth2UsernamePassword("read-user", "as87d6a", ClientId.from("client-001"), "ds65f");
        TokenResponseWrapper writeUserWrapper = iamAuthorizerClient.getAccessTokensOAuth2UsernamePassword("write-user", "6a57dfa", ClientId.from("client-001"), "ds65f");

        assertTrue(readUserWrapper.isOk());
        assertTrue(writeUserWrapper.isOk());
        readUserTokens = readUserWrapper.getTokenResponse();
        writeUserTokens = writeUserWrapper.getTokenResponse();
        LOG.info("PROJECT ADMIN: {}", projectAdminTokens.getAccessToken());
        LOG.info("READ USER: {}", readUserTokens.getAccessToken());
        LOG.info("WRITE USER: {}", writeUserTokens.getAccessToken());
    }

    @Test
    @Order(5)
    public void reloadKeyCache() {
        iamClient.updateKeyCache();
        Optional<StandardTokenClaims> readUserClaims = iamClient.validate(new JWToken(readUserTokens.getAccessToken()));
        assertTrue(readUserClaims.isPresent());
        Optional<StandardTokenClaims> writeUserClaims = iamClient.validate(new JWToken(writeUserTokens.getAccessToken()));
        assertTrue(writeUserClaims.isPresent());

        accessToken = readUserTokens.getAccessToken();
    }

    @Test
    @Order(21)
    public void testInitialServiceState() {
        EntityExchangeResult<DataSeriesInfo[]> entityExchangeResult = getDataSeriesInfo();
        assertEquals(0, entityExchangeResult.getResponseBody().length);
    }

    @Test
    @Order(22)
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
    @Order(23)
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
    @Order(24)
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
    @Order(25)
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
        private static GenericContainer<?> iamServiceContainer = new GenericContainer<>(IAM_SERVICE_DOCKER_IMAGE)
                .withExposedPorts(IAM_SERVICE_EXPOSED_PORT)
                .withReuse(true);

        @Override
        public void initialize(@NonNull ConfigurableApplicationContext context) {

            mongoDBContainer.start();
            Assertions.assertTrue(mongoDBContainer.isRunning());
            AppEventLoggerTests.mongoDBContainer = mongoDBContainer;
            Integer boundPort = mongoDBContainer.getMappedPort(DOCKER_EXPOSED_MONGO_PORT);

            LOGGER.info("MONGO      : {}", mongoDBContainer.getReplicaSetUrl());
            LOGGER.info("MONGO      : mongodb://localhost:{}/test", boundPort);

            iamServiceContainer.start();
            Assertions.assertTrue(iamServiceContainer.isRunning());

            String iamServiceBaseURL = "http://" + iamServiceContainer.getContainerIpAddress() + ":" + iamServiceContainer.getMappedPort(IAM_SERVICE_EXPOSED_PORT);
            LOGGER.info("IAM-SERVICE: {}:{}", iamServiceContainer.getContainerIpAddress(), iamServiceContainer.getMappedPort(IAM_SERVICE_EXPOSED_PORT));
            LOGGER.info("IAM-SERVICE: {}", iamServiceBaseURL);
            AppEventLoggerTests.iamServiceBaseURL = iamServiceBaseURL;

            TestPropertyValues.of(
                    "spring.data.mongodb.host=localhost",
                    "spring.data.mongodb.port=" + boundPort,
                    "spring.data.mongodb.database=test",
                    "app.iamClient.baseUrl=" + iamServiceBaseURL
            ).applyTo(context.getEnvironment());
        }
    }

}
