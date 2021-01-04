package one.microproject.logger.config;

import one.microproject.iamservice.core.dto.StandardTokenClaims;
import one.microproject.logger.config.security.AuthenticationImpl;
import one.microproject.logger.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class SecurityFilter implements WebFilter {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);

    private final SecurityService securityService;

    @Autowired
    public SecurityFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Optional<String> authorization = exchange.getRequest().getHeaders().get("Authorization").stream().findFirst();
        if (authorization.isPresent()) {
            Optional<StandardTokenClaims> standardTokenClaims = securityService.validate(authorization.get());
            if (standardTokenClaims.isPresent()) {
                LOG.info("filter: AUTHORIZED");
                ReactiveSecurityContextHolder.withAuthentication(new AuthenticationImpl(standardTokenClaims.get()));
                return chain.filter(exchange).contextWrite((c) -> ReactiveSecurityContextHolder.withAuthentication(new AuthenticationImpl(standardTokenClaims.get())));
            }
        }
        LOG.info("filter: UNAUTHORIZED");
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return chain.filter(exchange);
    }

}
