package one.microproject.logger.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.logger.model.DataRecord;
import one.microproject.logger.service.DataMapper;
import one.microproject.logger.tests.testdto.DataWrapper;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class DataRecordTests {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDataRecordSerialization() throws JsonProcessingException {
        DataWrapper dataWrapper = new DataWrapper(120, true, "example", DataWrapper.getInstance());
        JsonNode jsonNode = mapper.valueToTree(dataWrapper);
        DataRecord dataRecord = new DataRecord("01", 3600L, jsonNode);
        String data = mapper.writeValueAsString(dataRecord);
        assertNotNull(data);

        DataRecord deserializedRecord = mapper.readValue(data, DataRecord.class);
        assertEquals(dataRecord.getId(), deserializedRecord.getId());
        assertEquals(dataRecord.getTimeStamp(), deserializedRecord.getTimeStamp());

        DataWrapper dataWrapperDeserialized = mapper.treeToValue(deserializedRecord.getPayload(), DataWrapper.class);
        assertEquals(dataWrapper.getValue(), dataWrapperDeserialized.getValue());
        assertEquals(dataWrapper.getResult(), dataWrapperDeserialized.getResult());
        assertEquals(dataWrapper.getData(), dataWrapperDeserialized.getData());
        assertEquals(dataWrapper.getWrapper(), dataWrapperDeserialized.getWrapper());
    }

    @Test
    public void recordToDocumentTests() throws JsonProcessingException {
        DataWrapper dataWrapper = new DataWrapper(120, true, "example", DataWrapper.getInstance());
        JsonNode jsonNode = mapper.valueToTree(dataWrapper);
        DataRecord dataRecord = new DataRecord("01", 3600L, jsonNode);

        Document document = DataMapper.toDocument(mapper, dataRecord);
        DataRecord deserializedRecord = DataMapper.toDataRecord(mapper, document);

        assertEquals(dataRecord.getId(), deserializedRecord.getId());
        assertEquals(dataRecord.getTimeStamp(), deserializedRecord.getTimeStamp());

        DataWrapper dataWrapperDeserialized = mapper.treeToValue(deserializedRecord.getPayload(), DataWrapper.class);
        assertEquals(dataWrapper.getValue(), dataWrapperDeserialized.getValue());
        assertEquals(dataWrapper.getResult(), dataWrapperDeserialized.getResult());
        assertEquals(dataWrapper.getData(), dataWrapperDeserialized.getData());
        assertEquals(dataWrapper.getWrapper(), dataWrapperDeserialized.getWrapper());
    }

}
