package one.microproject.logger.service;

import one.microproject.logger.dto.CreateDataSeriesRequest;
import one.microproject.logger.dto.DeleteDataSeriesRequest;
import one.microproject.logger.dto.GenericResponse;
import one.microproject.logger.dto.DataSeriesInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DataSeriesService {

    Mono<GenericResponse> createDataSeries(CreateDataSeriesRequest request);

    Flux<DataSeriesInfo> getAll();

    Mono<GenericResponse> deleteDataSeries(DeleteDataSeriesRequest request);

}
