package one.microproject.logger.config.security;

import one.microproject.iamservice.client.IAMClient;
import one.microproject.iamservice.client.JWTUtils;
import one.microproject.iamservice.core.dto.StandardTokenClaims;
import one.microproject.iamservice.core.model.JWToken;
import one.microproject.logger.config.ContextConfig;
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

import java.util.List;
import java.util.Optional;

@Component
public class SecurityFilter implements WebFilter {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);
    private static final String ACTUATOR_PREFIX = "/actuator";

    private final IAMClient iamClient;
    private final ContextConfig config;

    @Autowired
    public SecurityFilter(IAMClient iamClient, ContextConfig config) {
        this.iamClient = iamClient;
        this.config = config;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        List<String> authorizationHeaders = exchange.getRequest().getHeaders().get("Authorization");
        String path = exchange.getRequest().getPath().toString();
        if (path.startsWith(config.getPath(ACTUATOR_PREFIX))) {
            LOG.info("filter: AUTHORIZATION Skipped: {}", path);
            return chain.filter(exchange);
        }
        if (authorizationHeaders != null) {
            Optional<String> authorization = authorizationHeaders.stream().findFirst();
            if (authorization.isPresent()) {
                JWToken jwToken = JWTUtils.extractJwtToken(authorization.get());
                Optional<StandardTokenClaims> standardTokenClaims = iamClient.validate(jwToken);
                if (standardTokenClaims.isPresent()) {
                    LOG.info("filter: AUTHORIZED");
                    ReactiveSecurityContextHolder.withAuthentication(new AuthenticationImpl(standardTokenClaims.get()));
                    return chain.filter(exchange).contextWrite(c -> ReactiveSecurityContextHolder.withAuthentication(new AuthenticationImpl(standardTokenClaims.get())));
                }
            }
        }
        LOG.info("filter: UNAUTHORIZED {}", path);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return Mono.empty();
    }

}
