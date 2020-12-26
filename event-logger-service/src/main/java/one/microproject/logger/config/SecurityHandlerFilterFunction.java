package one.microproject.logger.config;

import one.microproject.logger.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class SecurityHandlerFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityHandlerFilterFunction.class);

    private final SecurityService securityService;

    public SecurityHandlerFilterFunction(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public Mono<ServerResponse> filter(ServerRequest serverRequest, HandlerFunction<ServerResponse> handlerFunction) {
        Optional<String> authorization = serverRequest.headers().header("Authorization").stream().findFirst();
        if (authorization.isPresent() && securityService.validate(authorization.get())) {
            return handlerFunction.handle(serverRequest);
        } else {
            LOG.info("filter: UNAUTHORIZED");
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
