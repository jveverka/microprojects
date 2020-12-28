package one.microproject.logger.service;

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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
public class DataRecordServiceImpl implements DataRecordService {

    private static final Logger LOG = LoggerFactory.getLogger(DataRecordServiceImpl.class);

    private final MongoDatabase mongoDatabase;

    @Autowired
    public DataRecordServiceImpl(MongoClient mongoClient, MongoConfig mongoConfig) {
        this.mongoDatabase = mongoClient.getDatabase(mongoConfig.getDatabase());
    }

    @Override
    public Mono<GenericResponse> save(InsertDataRecord insertDataRecord) {
        LOG.info("save: {}:{}", insertDataRecord.getGroupId(), insertDataRecord.getName());
        DataSeriesId id = DataSeriesId.from(insertDataRecord.getGroupId(), insertDataRecord.getName());
        DataRecord record =  new DataRecord(UUID.randomUUID().toString(), insertDataRecord.getTimeStamp(), insertDataRecord.getPayload());
        return save(id, record);
    }

    @Override
    public Mono<GenericResponse> save(CreateDataRecord createDataRecord) {
        LOG.info("save: {}:{}", createDataRecord.getGroupId(), createDataRecord.getName());
        DataSeriesId id = DataSeriesId.from(createDataRecord.getGroupId(), createDataRecord.getName());
        DataRecord record =  new DataRecord(UUID.randomUUID().toString(), Instant.now().getEpochSecond(), createDataRecord.getPayload());
        return save(id, record);
    }

    @Override
    public Mono<GenericResponse> save(DataSeriesId id, DataRecord record) {
        Publisher<InsertOneResult> insertOneResultPublisher = mongoDatabase.getCollection(id.getName()).insertOne(toDocument(record));
        Mono<InsertOneResult> insertOneResultMono =  Mono.from(insertOneResultPublisher);
        return insertOneResultMono.transform( mono -> mono.map( i -> GenericResponse.ok() ) );
    }

    @Override
    public Flux<DataRecord> getAll(DataSeriesId id) {
        LOG.info("getAll");
        FindPublisher<Document> publisher = mongoDatabase.getCollection(id.getName()).find();
        Flux<Document> fluxDocument = Flux.from(publisher);
        return fluxDocument.transform( flux -> flux.map( d -> toDataRecord(d) ) );
    }

    @Override
    public Flux<DataRecord> get(DataSeriesId id, Long beginTime, Long duration) {
        LOG.info("get: {}:{}:{}", id.toStringId(), beginTime, duration);
        return null;
    }

    @Override
    public void dropAll(DataSeriesId id) {
        LOG.info("dropAll: {}", id.toStringId());
        mongoDatabase.getCollection(id.getName()).drop();
    }

    public static Document toDocument(DataRecord record) {
        Document document = new Document();
        document.append("_id", record.getId());
        document.append("timeStamp", record.getTimeStamp());
        //TODO: implement payload conversion
        return document;
    }

    public static DataRecord toDataRecord(Document document) {
        String id = document.get("_id", String.class);
        Long timeStamp = document.get("timeStamp", Long.class);
        //TODO: implement payload conversion
        return new DataRecord(id, timeStamp, null);
    }

}
