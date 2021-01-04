package one.microproject.logger.service;

import one.microproject.iamservice.core.dto.StandardTokenClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthorizerService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizerService.class);

    public boolean hasAccess(String key, Authentication authentication) {
        LOG.info("hasAccess: {}:{}", key, authentication.getName());
        StandardTokenClaims standardTokenClaims = (StandardTokenClaims)authentication.getDetails();
        return true;
    }

}
