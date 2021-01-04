package one.microproject.logger.config;

import one.microproject.logger.dto.CreateDataRecord;
import one.microproject.logger.dto.CreateDataSeriesRequest;
import one.microproject.logger.dto.DataSeriesInfo;
import one.microproject.logger.dto.GenericResponse;
import one.microproject.logger.dto.InsertDataRecord;
import one.microproject.logger.model.DataRecord;
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
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> route(DataSeriesService dataSeriesService,
                                                DataRecordService dataRecordService) {
        return RouterFunctions
                //Data Series - APIs
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
                // Data Records - APIs
                .andRoute(GET("/services/records/{groupId}/{name}").and(accept(APPLICATION_JSON)), request -> {
                    DataSeriesId id =
                            new DataSeriesId(request.pathVariable("groupId"), request.pathVariable("name"));
                    Flux<DataRecord> dataRecordFlux = dataRecordService.getAll(id);
                    return ServerResponse.ok().body(dataRecordFlux, DataRecord.class);
                })
                .andRoute(GET("/services/records/{groupId}/{name}/{startTime}/{duration}").and(accept(APPLICATION_JSON)), request -> {
                    DataSeriesId id =
                            new DataSeriesId(request.pathVariable("groupId"), request.pathVariable("name"));
                    Long startTime = Long.parseLong(request.pathVariable("startTime"));
                    Long duration = Long.parseLong(request.pathVariable("duration"));
                    Flux<DataRecord> dataRecordFlux = dataRecordService.get(id, startTime, duration);
                    return ServerResponse.ok().body(dataRecordFlux, DataRecord.class);
                })
                .andRoute(POST("/services/records").and(accept(APPLICATION_JSON)), request -> {
                    Mono<CreateDataRecord> monoBody = request.bodyToMono(CreateDataRecord.class);
                    Mono<GenericResponse> genericResponseMono = monoBody.flatMap(dataRecordService::save);
                    return ServerResponse.ok().body(genericResponseMono, GenericResponse.class);
                })
                .andRoute(PUT("/services/records").and(accept(APPLICATION_JSON)), request -> {
                    Mono<InsertDataRecord> monoBody = request.bodyToMono(InsertDataRecord.class);
                    Mono<GenericResponse> genericResponseMono = monoBody.flatMap(dataRecordService::save);
                    return ServerResponse.ok().body(genericResponseMono, GenericResponse.class);
                })
                .andRoute(DELETE("/services/records/{groupId}/{name}/{timeStamp}").and(accept(APPLICATION_JSON)), request -> {
                    DataSeriesId id =
                            new DataSeriesId(request.pathVariable("groupId"), request.pathVariable("name"));
                    Long timeStamp = Long.parseLong(request.pathVariable("timeStamp"));
                    Mono<GenericResponse> genericResponseMono = dataRecordService.delete(id, timeStamp);
                    return ServerResponse.ok().body(genericResponseMono, GenericResponse.class);
                });
    }

}
