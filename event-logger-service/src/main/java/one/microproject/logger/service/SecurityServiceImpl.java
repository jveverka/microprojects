package one.microproject.logger.service;

import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Override
    public boolean validate(String authorization) {
        return true;
    }

}
