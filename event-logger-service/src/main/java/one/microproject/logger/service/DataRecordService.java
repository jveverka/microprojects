package one.microproject.logger.service;

import one.microproject.logger.dto.CreateDataRecord;
import one.microproject.logger.dto.GenericResponse;
import one.microproject.logger.dto.InsertDataRecord;
import one.microproject.logger.model.DataRecord;
import one.microproject.logger.model.DataSeriesId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DataRecordService {

    Mono<GenericResponse> save(InsertDataRecord insertDataRecord);

    Mono<GenericResponse> save(CreateDataRecord createDataRecord);

    Mono<GenericResponse> save(DataSeriesId id, DataRecord record);

    Flux<DataRecord> getAll(DataSeriesId id);

    Flux<DataRecord> get(DataSeriesId id, Long beginTime, Long duration);

    Mono<GenericResponse> delete(DataSeriesId id, Long timeStamp);

    void dropAll(DataSeriesId id);

}
