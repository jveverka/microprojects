package one.microproject.logger.service;

import one.microproject.iamservice.core.dto.StandardTokenClaims;
import one.microproject.logger.dto.CreateDataRecord;
import one.microproject.logger.dto.CreateDataSeriesRequest;
import one.microproject.logger.dto.InsertDataRecord;
import one.microproject.logger.model.DataSeriesId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthorizerService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizerService.class);

    public boolean hasAccess(String resourceType, String action, InsertDataRecord insert, Authentication authentication) {
        DataSeriesId id = DataSeriesId.from(insert.getGroupId(), insert.getName());
        return hasAccess(resourceType, action, id, authentication);
    }

    public boolean hasAccess(String resourceType, String action, CreateDataRecord create, Authentication authentication) {
        DataSeriesId id = DataSeriesId.from(create.getGroupId(), create.getName());
        return hasAccess(resourceType, action, id, authentication);
    }

    public boolean hasAccess(String resourceType, String action, CreateDataSeriesRequest request, Authentication authentication) {
        DataSeriesId id = DataSeriesId.from(request.getGroupId(), request.getName());
        return hasAccess(resourceType, action, id, authentication);
    }

    public boolean hasAccess(String resourceType, String action, Authentication authentication) {
        return hasAccess(resourceType, action, DataSeriesId.EMPTY, authentication);
    }

    public boolean hasAccess(String resourceType, String action, DataSeriesId id, Authentication authentication) {
        StandardTokenClaims standardTokenClaims = (StandardTokenClaims)authentication.getDetails();
        LOG.info("hasAccess: {}.{}.{} # {}:{}", resourceType, id.toStringId(), action, authentication.getName(), standardTokenClaims.getScope());
        //TODO: implement scope -> key access matching
        return true;
    }

}
