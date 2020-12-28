package one.microproject.logger.service;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoDatabase;
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

    private final MongoDatabase mongoDatabase;

    @Autowired
    public DataRecordServiceImpl(MongoClient mongoClient, MongoConfig mongoConfig) {
        this.mongoDatabase = mongoClient.getDatabase(mongoConfig.getDatabase());
    }

    @Override
    public Mono<GenericResponse> save(DataSeriesId id, DataRecord record) {
        //mongoDatabase.getCollection(id.getName()).insertOne();
        return null;
    }

    @Override
    public Flux<DataRecord> getAll(DataSeriesId id) {
        mongoDatabase.getCollection(id.getName()).find();
        return null;
    }

    @Override
    public Flux<DataRecord> get(DataSeriesId id, Long beginTime, Long duration) {
        return null;
    }

    @Override
    public void dropAll(DataSeriesId id) {
        mongoDatabase.getCollection(id.getName()).drop();
    }

}
