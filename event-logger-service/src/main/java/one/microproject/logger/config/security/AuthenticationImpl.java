package one.microproject.logger.config.security;

import one.microproject.iamservice.core.dto.StandardTokenClaims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

public class AuthenticationImpl implements Authentication {

    private final StandardTokenClaims standardTokenClaims;
    private final Collection<? extends GrantedAuthority> grantedAuthorities;

    public AuthenticationImpl(StandardTokenClaims standardTokenClaims) {
        this.standardTokenClaims = standardTokenClaims;
        this.grantedAuthorities = standardTokenClaims.getScope()
                .stream().map(s -> new GrantedAuthorityImpl(s)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return standardTokenClaims;
    }

    @Override
    public Object getPrincipal() {
        return standardTokenClaims.getSubject();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

    @Override
    public String getName() {
        return standardTokenClaims.getSubject();
    }

}
