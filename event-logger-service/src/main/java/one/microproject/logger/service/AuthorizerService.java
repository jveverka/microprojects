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
        StandardTokenClaims standardTokenClaims = (StandardTokenClaims)authentication.getDetails();
        LOG.info("hasAccess: {} / {}:{}", key, authentication.getName(), standardTokenClaims.getScope());
        //TODO: implement scope -> key access matching
        return true;
    }

}
