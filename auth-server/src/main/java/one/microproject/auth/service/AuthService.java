package one.microproject.auth.service;

import one.microproject.auth.dto.IntrospectRequest;
import one.microproject.auth.dto.IntrospectResponse;
import one.microproject.auth.dto.TokenResponse;
import one.microproject.auth.dto.UserAuthRequest;

public interface AuthService {

    TokenResponse token(UserAuthRequest request);

    IntrospectResponse introspect(IntrospectRequest request);

}
