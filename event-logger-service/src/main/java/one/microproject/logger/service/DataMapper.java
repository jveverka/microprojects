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

    public static Document toDocument(ObjectMapper mapper, DataRecord record) {
        Document document = new Document();
        document.append("_id", record.getId());
        document.append("timeStamp", record.getTimeStamp());
        try {
            String jsonData = mapper.writeValueAsString(record.getPayload());
            Document payload = Document.parse(jsonData);
            document.append("payload", payload);
        } catch (IOException e) {
            LOG.error("Error: ", e);
        }
        return document;
    }

    public static DataRecord toDataRecord(ObjectMapper mapper, Document document) {
        String id = document.get("_id", String.class);
        Long timeStamp = document.get("timeStamp", Long.class);
        try {
            Document payloadDocument = document.get("payload", Document.class);
            JsonNode payload = mapper.readTree(payloadDocument.toJson());
            return new DataRecord(id, timeStamp, payload);
        } catch (IOException e) {
            LOG.error("Error: ", e);
            return new DataRecord(id, timeStamp, null);
        }
    }

}
