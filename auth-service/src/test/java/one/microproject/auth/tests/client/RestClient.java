package one.microproject.auth.tests.client;

import one.microproject.auth.dto.*;

import java.util.List;

public interface RestClient {

    ServerResponse<TokenResponse> token(UserAuthRequest request);

    ServerResponse<IntrospectResponse> introspect(IntrospectRequest request);

    ServerResponse<List<UserData>> getAll();

}
