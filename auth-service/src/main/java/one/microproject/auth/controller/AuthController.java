package one.microproject.auth.controller;

import one.microproject.auth.dto.*;
import one.microproject.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = URLs.AUTH_TOKEN_GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServerResponse<TokenResponse>> token(@RequestBody UserAuthRequest request) {
        TokenResponse response = authService.token(request);
        return ResponseEntity.ok(ServerResponse.ok(response));
    }

    @PostMapping(path = URLs.AUTH_TOKEN_INTROSPECT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServerResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request) {
        IntrospectResponse response = authService.introspect(request);
        return ResponseEntity.ok(ServerResponse.ok(response));
    }

}
