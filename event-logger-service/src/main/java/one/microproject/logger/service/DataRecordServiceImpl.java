package one.microproject.logger.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoDatabase;
import one.microproject.logger.config.MongoConfig;
import one.microproject.logger.dto.CreateDataRecord;
import one.microproject.logger.dto.GenericResponse;
import one.microproject.logger.dto.InsertDataRecord;
import one.microproject.logger.model.DataRecord;
import one.microproject.logger.model.DataSeriesId;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gte;
import static one.microproject.logger.service.DataMapper.TIME_STAMP;
import static one.microproject.logger.service.DataMapper.toDataRecord;
import static one.microproject.logger.service.DataMapper.toDocument;

@Service
public class DataRecordServiceImpl implements DataRecordService {

    private static final Logger LOG = LoggerFactory.getLogger(DataRecordServiceImpl.class);

    private final MongoDatabase mongoDatabase;
    private final ObjectMapper mapper;

    @Autowired
    public DataRecordServiceImpl(MongoClient mongoClient, MongoConfig mongoConfig, ObjectMapper mapper) {
        this.mongoDatabase = mongoClient.getDatabase(mongoConfig.getDatabase());
        this.mapper = mapper;
    }

    @Override
    @PreAuthorize("@authorizerService.hasAccess('DataRecord', 'save', #insertDataRecord, authentication)")
    public Mono<GenericResponse> save(InsertDataRecord insertDataRecord) {
        LOG.info("save: {}:{}", insertDataRecord.getGroupId(), insertDataRecord.getName());
        DataSeriesId id = DataSeriesId.from(insertDataRecord.getGroupId(), insertDataRecord.getName());
        DataRecord record =  new DataRecord(UUID.randomUUID().toString(), insertDataRecord.getTimeStamp(), insertDataRecord.getPayload());
        return save(id, record);
    }

    @Override
    @PreAuthorize("@authorizerService.hasAccess('DataRecord', 'save', #createDataRecord, authentication)")
    public Mono<GenericResponse> save(CreateDataRecord createDataRecord) {
        LOG.info("save: {}:{}", createDataRecord.getGroupId(), createDataRecord.getName());
        DataSeriesId id = DataSeriesId.from(createDataRecord.getGroupId(), createDataRecord.getName());
        DataRecord record =  new DataRecord(UUID.randomUUID().toString(), Instant.now().getEpochSecond(), createDataRecord.getPayload());
        return save(id, record);
    }

    @Override
    @PreAuthorize("@authorizerService.hasAccess('DataRecord', 'save', #id, authentication)")
    public Mono<GenericResponse> save(DataSeriesId id, DataRecord record) {
        Publisher<InsertOneResult> insertOneResultPublisher = mongoDatabase.getCollection(id.getName()).insertOne(toDocument(mapper, record));
        Mono<InsertOneResult> insertOneResultMono =  Mono.from(insertOneResultPublisher);
        return insertOneResultMono.transform( mono -> mono.map( i -> GenericResponse.ok() ) );
    }

    @Override
    @PreAuthorize("@authorizerService.hasAccess('DataRecord', 'getAll', #id, authentication)")
    public Flux<DataRecord> getAll(DataSeriesId id) {
        LOG.info("getAll");
        FindPublisher<Document> publisher = mongoDatabase.getCollection(id.getName()).find();
        Flux<Document> fluxDocument = Flux.from(publisher);
        return fluxDocument.transform( flux -> flux.map( d -> toDataRecord(mapper, d) ) );
    }

    @Override
    @PreAuthorize("@authorizerService.hasAccess('DataRecord', 'getRecord', #id, authentication)")
    public Flux<DataRecord> get(DataSeriesId id, Long beginTime, Long duration) {
        LOG.info("get: {}:{}:{}", id.toStringId(), beginTime, duration);
        Long endTime = beginTime + duration;
        FindPublisher<Document> publisher = mongoDatabase.getCollection(id.getName())
                .find(and(gte(TIME_STAMP, beginTime), lt(TIME_STAMP,  endTime)));
        Flux<Document> fluxDocument = Flux.from(publisher);
        return fluxDocument.transform( flux -> flux.map( d -> toDataRecord(mapper, d) ) );
    }

    @Override
    @PreAuthorize("@authorizerService.hasAccess('DataRecord', 'delete', #id, authentication)")
    public Mono<GenericResponse> delete(DataSeriesId id, Long timeStamp) {
        LOG.info("delete: {}:{}", id.toStringId(), timeStamp);
        Publisher<DeleteResult> deleteOne = mongoDatabase.getCollection(id.getName()).deleteOne(eq(TIME_STAMP, timeStamp));
        Mono<DeleteResult> deleteResultMono =  Mono.from(deleteOne);
        return deleteResultMono.transform( mono -> mono.map( i -> GenericResponse.ok() ) );
    }

    @Override
    public void dropAll(DataSeriesId id) {
        LOG.info("dropAll: {}", id.toStringId());
        mongoDatabase.getCollection(id.getName()).drop();
    }

}
