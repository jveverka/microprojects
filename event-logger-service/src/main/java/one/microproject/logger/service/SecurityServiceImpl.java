package one.microproject.logger.service;

import one.microproject.iamservice.client.IAMClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityServiceImpl.class);

    private final IAMClient iamClient;

    @Autowired
    public SecurityServiceImpl(IAMClient iamClient) {
        this.iamClient = iamClient;
    }

    @Override
    public boolean validate(String authorization) {
        LOG.debug("authorization: {}",  authorization);
        return true;
    }

}
