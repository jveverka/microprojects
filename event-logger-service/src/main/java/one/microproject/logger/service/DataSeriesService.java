package one.microproject.logger.service;

import one.microproject.logger.dto.CreateDataSeriesRequest;
import one.microproject.logger.dto.GenericResponse;
import one.microproject.logger.dto.DataSeriesInfo;
import one.microproject.logger.model.DataSeriesId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DataSeriesService {

    Mono<GenericResponse> createDataSeries(CreateDataSeriesRequest request);

    Flux<DataSeriesInfo> getAll();

    Mono<GenericResponse> deleteDataSeries(DataSeriesId id);

    Mono<DataSeriesInfo> get(DataSeriesId id);

}
