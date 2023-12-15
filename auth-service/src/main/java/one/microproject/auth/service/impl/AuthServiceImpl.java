package one.microproject.auth.service.impl;

import one.microproject.auth.dto.IntrospectRequest;
import one.microproject.auth.dto.IntrospectResponse;
import one.microproject.auth.dto.TokenResponse;
import one.microproject.auth.dto.UserAuthRequest;
import one.microproject.auth.dto.UserData;
import one.microproject.auth.exceptions.AuthException;
import one.microproject.auth.service.AuthService;
import one.microproject.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    @Autowired
    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public TokenResponse token(UserAuthRequest request) {
        Optional<UserData> userData = userService.get(request.username());
        if (userData.isPresent() && request.password().equals(userData.get().password())) {
            return new TokenResponse("valid");
        }
        throw new AuthException("User not valid.");
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        if ("valid".equals(request.token())) {
            return new IntrospectResponse(true);
        } else {
            return new IntrospectResponse(false);
        }
    }

}
