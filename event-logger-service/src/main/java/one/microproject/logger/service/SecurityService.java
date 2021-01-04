package one.microproject.logger.service;

import one.microproject.iamservice.core.dto.StandardTokenClaims;

import java.util.Optional;

public interface SecurityService {

    Optional<StandardTokenClaims> validate(String authorization);

}
