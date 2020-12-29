package one.microproject.logger.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.logger.model.DataRecord;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DataMapper {

    private static final Logger LOG = LoggerFactory.getLogger(DataMapper.class);

    private DataMapper() {
    }

    public static final String ID = "_id";
    public static final String TIME_STAMP = "timeStamp";
    public static final String PAYLOAD = "payload";

    public static Document toDocument(ObjectMapper mapper, DataRecord record) {
        Document document = new Document();
        document.append(ID, record.getId());
        document.append(TIME_STAMP, record.getTimeStamp());
        try {
            String jsonData = mapper.writeValueAsString(record.getPayload());
            Document payload = Document.parse(jsonData);
            document.append(PAYLOAD, payload);
        } catch (IOException e) {
            LOG.error("Error: ", e);
        }
        return document;
    }

    public static DataRecord toDataRecord(ObjectMapper mapper, Document document) {
        String id = document.get(ID, String.class);
        Long timeStamp = document.get(TIME_STAMP, Long.class);
        try {
            Document payloadDocument = document.get(PAYLOAD, Document.class);
            JsonNode payload = mapper.readTree(payloadDocument.toJson());
            return new DataRecord(id, timeStamp, payload);
        } catch (IOException e) {
            LOG.error("Error: ", e);
            return new DataRecord(id, timeStamp, null);
        }
    }

}
