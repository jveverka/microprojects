package one.microproject.logger.service;

import com.mongodb.reactivestreams.client.MongoClient;
import one.microproject.logger.config.MongoConfig;
import one.microproject.logger.dto.GenericResponse;
import one.microproject.logger.model.DataRecord;
import one.microproject.logger.model.DataSeriesId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DataRecordServiceImpl implements DataRecordService {

    private final MongoClient mongoClient;

    @Autowired
    public DataRecordServiceImpl(MongoClient mongoClient, MongoConfig mongoConfig) {
        this.mongoClient = mongoClient;
        this.mongoClient.getDatabase(mongoConfig.getDatabase());
    }

    @Override
    public Mono<GenericResponse> save(DataSeriesId id, DataRecord record) {
        return null;
    }

    @Override
    public Flux<DataRecord> getAll(DataSeriesId id) {
        return null;
    }

    @Override
    public Flux<DataRecord> get(DataSeriesId id, Long beginTime, Long duration) {
        return null;
    }

    @Override
    public void dropAll(DataSeriesId id) {

    }

}
