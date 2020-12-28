package one.microproject.logger.service;

import one.microproject.logger.dto.GenericResponse;
import one.microproject.logger.model.DataRecord;
import one.microproject.logger.model.DataSeriesId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DataRecordService {

    Mono<GenericResponse> save(DataSeriesId id, DataRecord record);

    Flux<DataRecord> getAll(DataSeriesId id);

    Flux<DataRecord> get(DataSeriesId id, Long beginTime, Long duration);

}
