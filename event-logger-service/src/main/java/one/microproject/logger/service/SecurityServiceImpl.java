package one.microproject.logger.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityServiceImpl.class);

    @Override
    public boolean validate(String authorization) {
        LOG.info("authorization: {}",  authorization);
        return true;
    }

}
