package one.microproject.logger.service;

import one.microproject.iamservice.client.IAMClient;
import one.microproject.iamservice.client.JWTUtils;
import one.microproject.iamservice.core.dto.StandardTokenClaims;
import one.microproject.iamservice.core.model.JWToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityServiceImpl implements SecurityService {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityServiceImpl.class);

    private final IAMClient iamClient;

    @Autowired
    public SecurityServiceImpl(IAMClient iamClient) {
        this.iamClient = iamClient;
    }

    @Override
    public Optional<StandardTokenClaims> validate(String authorization) {
        LOG.debug("authorization: {}",  authorization);
        JWToken jwToken = JWTUtils.extractJwtToken(authorization);
        return iamClient.validate(jwToken);
    }

}
