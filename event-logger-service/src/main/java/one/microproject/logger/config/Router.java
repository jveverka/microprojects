package one.microproject.logger.config;

import one.microproject.logger.dto.CreateDataSeriesRequest;
import one.microproject.logger.dto.DataSeriesInfo;
import one.microproject.logger.dto.GenericResponse;
import one.microproject.logger.model.DataSeriesId;
import one.microproject.logger.service.DataRecordService;
import one.microproject.logger.service.DataSeriesService;
import one.microproject.logger.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> route(SecurityService securityService,
                                                DataSeriesService dataSeriesService, DataRecordService dataRecordService) {
        return RouterFunctions
                .route(GET("/services/series").and(accept(APPLICATION_JSON)), request -> {
                    Flux<DataSeriesInfo> dataSeriesInfoFlux = dataSeriesService.getAll();
                    return ServerResponse.ok().body(dataSeriesInfoFlux, DataSeriesInfo.class);
                })
                .andRoute(POST("/services/series").and(accept(APPLICATION_JSON)), request -> {
                    Mono<CreateDataSeriesRequest> monoBody = request.bodyToMono(CreateDataSeriesRequest.class);
                    Mono<GenericResponse> genericResponseMono = monoBody.flatMap(dataSeriesService::createDataSeries);
                    return ServerResponse.ok().body(genericResponseMono, GenericResponse.class);
                })
                .andRoute(DELETE("/services/series/{groupId}/{name}").and(accept(APPLICATION_JSON)), request -> {
                    DataSeriesId id =
                            new DataSeriesId(request.pathVariable("groupId"), request.pathVariable("name"));
                    Mono<GenericResponse> genericResponseMono = dataSeriesService.deleteDataSeries(id);
                    return ServerResponse.ok().body(genericResponseMono, GenericResponse.class);
                })
                .andRoute(GET("/services/series/{groupId}/{name}").and(accept(APPLICATION_JSON)), request -> {
                    DataSeriesId id =
                            new DataSeriesId(request.pathVariable("groupId"), request.pathVariable("name"));
                    Mono<DataSeriesInfo> dataSeriesInfoMono = dataSeriesService.get(id);
                    return ServerResponse.ok().body(dataSeriesInfoMono, DataSeriesInfo.class);
                })
                .filter(new SecurityHandlerFilterFunction(securityService));
    }

}
