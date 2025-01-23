package one.microproject.auth.tests;

import one.microproject.auth.AuthServiceApp;
import one.microproject.auth.tests.client.RestClient;
import one.microproject.auth.tests.client.RestClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = AuthServiceApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AuthServiceTestBase {

    @Autowired
    Environment environment;

    public RestClient getRestClient() {
        return new RestClientImpl(environment);
    }

}
