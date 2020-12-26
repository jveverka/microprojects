package one.microproject.logger.config;

import one.microproject.logger.service.DataRecordService;
import one.microproject.logger.service.DataSeriesService;
import one.microproject.logger.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> route(SecurityService securityService,
                                                DataSeriesService dataSeriesService, DataRecordService dataRecordService) {
        return RouterFunctions
                .route()
                .filter(new SecurityHandlerFilterFunction(securityService))
                .build();
    }

}
