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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class Router {

    private static final Logger LOG = LoggerFactory.getLogger(Router.class);

    private static final String GROUP_ID = "groupId";
    private static final String NAME = "name";
    private static final String TIME_STAMP = "timeStamp";
    private static final String START_TIME = "startTime";
    private static final String DURATION = "duration";

    @Value("${app.context-path}")
    private String contextPath;
    private String normalizedContextPath;

    @PostConstruct
    private void normalizeContextPath() {
        if (contextPath == null) {
            normalizedContextPath = "";
        } else {
            contextPath = contextPath.trim();
            if (contextPath.isEmpty() || "/".equals(contextPath)) {
                normalizedContextPath = "";
            } else if (!contextPath.startsWith("/")) {
                normalizedContextPath = "/" + contextPath;
            } else {
                normalizedContextPath = contextPath;
            }
        }
        LOG.info("## INIT: contextPath=\"{}\"", contextPath);
        LOG.info("## INIT: normalizedContextPath=\"{}\"", normalizedContextPath);
    }

    @Bean
    public RouterFunction<ServerResponse> route(DataSeriesService dataSeriesService,
                                                DataRecordService dataRecordService) {
        return RouterFunctions
                // Data Series - APIs
                .route(GET(normalizedContextPath + "/services/series").and(accept(APPLICATION_JSON)), request -> {
                    Flux<DataSeriesInfo> dataSeriesInfoFlux = dataSeriesService.getAll();
                    return ServerResponse.ok().body(dataSeriesInfoFlux, DataSeriesInfo.class);
                })
                .andRoute(POST(normalizedContextPath + "/services/series").and(accept(APPLICATION_JSON)), request -> {
                    Mono<CreateDataSeriesRequest> monoBody = request.bodyToMono(CreateDataSeriesRequest.class);
                    Mono<GenericResponse> genericResponseMono = monoBody.flatMap(dataSeriesService::createDataSeries);
                    return ServerResponse.ok().body(genericResponseMono, GenericResponse.class);
                })
                .andRoute(DELETE(normalizedContextPath + "/services/series/{groupId}/{name}").and(accept(APPLICATION_JSON)), request -> {
                    DataSeriesId id =
                            new DataSeriesId(request.pathVariable(GROUP_ID), request.pathVariable(NAME));
                    Mono<GenericResponse> genericResponseMono = dataSeriesService.deleteDataSeries(id);
                    return ServerResponse.ok().body(genericResponseMono, GenericResponse.class);
                })
                .andRoute(GET(normalizedContextPath + "/services/series/{groupId}/{name}").and(accept(APPLICATION_JSON)), request -> {
                    DataSeriesId id =
                            new DataSeriesId(request.pathVariable(GROUP_ID), request.pathVariable(NAME));
                    Mono<DataSeriesInfo> dataSeriesInfoMono = dataSeriesService.get(id);
                    return ServerResponse.ok().body(dataSeriesInfoMono, DataSeriesInfo.class);
                })
                // Data Records - APIs
                .andRoute(GET(normalizedContextPath + "/services/records/{groupId}/{name}").and(accept(APPLICATION_JSON)), request -> {
                    DataSeriesId id =
                            new DataSeriesId(request.pathVariable(GROUP_ID), request.pathVariable(NAME));
                    Flux<DataRecord> dataRecordFlux = dataRecordService.getAll(id);
                    return ServerResponse.ok().body(dataRecordFlux, DataRecord.class);
                })
                .andRoute(GET(normalizedContextPath + "/services/records/{groupId}/{name}/{startTime}/{duration}").and(accept(APPLICATION_JSON)), request -> {
                    DataSeriesId id =
                            new DataSeriesId(request.pathVariable(GROUP_ID), request.pathVariable(NAME));
                    Long startTime = Long.parseLong(request.pathVariable(START_TIME));
                    Long duration = Long.parseLong(request.pathVariable(DURATION));
                    Flux<DataRecord> dataRecordFlux = dataRecordService.get(id, startTime, duration);
                    return ServerResponse.ok().body(dataRecordFlux, DataRecord.class);
                })
                .andRoute(POST(normalizedContextPath + "/services/records").and(accept(APPLICATION_JSON)), request -> {
                    Mono<CreateDataRecord> monoBody = request.bodyToMono(CreateDataRecord.class);
                    Mono<GenericResponse> genericResponseMono = monoBody.flatMap(dataRecordService::save);
                    return ServerResponse.ok().body(genericResponseMono, GenericResponse.class);
                })
                .andRoute(PUT(normalizedContextPath + "/services/records").and(accept(APPLICATION_JSON)), request -> {
                    Mono<InsertDataRecord> monoBody = request.bodyToMono(InsertDataRecord.class);
                    Mono<GenericResponse> genericResponseMono = monoBody.flatMap(dataRecordService::save);
                    return ServerResponse.ok().body(genericResponseMono, GenericResponse.class);
                })
                .andRoute(DELETE(normalizedContextPath + "/services/records/{groupId}/{name}/{timeStamp}").and(accept(APPLICATION_JSON)), request -> {
                    DataSeriesId id =
                            new DataSeriesId(request.pathVariable(GROUP_ID), request.pathVariable(NAME));
                    Long timeStamp = Long.parseLong(request.pathVariable(TIME_STAMP));
                    Mono<GenericResponse> genericResponseMono = dataRecordService.delete(id, timeStamp);
                    return ServerResponse.ok().body(genericResponseMono, GenericResponse.class);
                });
    }

}
