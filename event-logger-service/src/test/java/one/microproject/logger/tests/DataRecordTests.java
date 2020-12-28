package one.microproject.logger.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.logger.model.DataRecord;
import one.microproject.logger.tests.testdto.DataWrapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class DataRecordTests {

    @Test
    public void testDataRecordSerialization() throws JsonProcessingException {
        DataWrapper dataWrapper = new DataWrapper(120);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.valueToTree(dataWrapper);
        DataRecord dataRecord = new DataRecord("01", 3600L, jsonNode);
        String data = mapper.writeValueAsString(dataRecord);
        assertNotNull(data);

        DataRecord deserializedRecord = mapper.readValue(data, DataRecord.class);
        assertEquals(dataRecord.getId(), deserializedRecord.getId());
        assertEquals(dataRecord.getTimeStamp(), deserializedRecord.getTimeStamp());

        DataWrapper dataWrapperDeserialized = mapper.treeToValue(deserializedRecord.getPayload(), DataWrapper.class);
        assertEquals(dataWrapper.getValue(), dataWrapperDeserialized.getValue());
    }

}
