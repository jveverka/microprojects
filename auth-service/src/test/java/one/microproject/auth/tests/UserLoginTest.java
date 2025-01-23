package one.microproject.auth.tests;

import one.microproject.auth.dto.*;
import one.microproject.auth.tests.client.RestClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserLoginTest extends AuthServiceTestBase {

    @Test
    void testUserLogin() {
        RestClient restClient = getRestClient();
        ServerResponse<TokenResponse> loginResponse = restClient.token(new UserAuthRequest("juraj", "secret"));
        assertNotNull(loginResponse.data());
        assertNotNull(loginResponse.data().accessToken());

        ServerResponse<IntrospectResponse> introspectResponse = restClient.introspect(new IntrospectRequest(loginResponse.data().accessToken()));
        assertNotNull(introspectResponse.data());
        assertTrue(introspectResponse.data().active());
    }

}
