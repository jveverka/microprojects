package one.microproject.logger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class DataRecord {

    private final String id;
    private final Long timeStamp;
    private final JsonNode payload;

    @JsonCreator
    public DataRecord(@JsonProperty("id") String id,
                      @JsonProperty("timeStamp") Long timeStamp,
                      @JsonProperty("payload") JsonNode payload) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.payload = payload;
    }

    public String getId() {
        return id;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public JsonNode getPayload() {
        return payload;
    }

}
