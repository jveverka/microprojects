package one.microproject.logger.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class CreateDataRecord {

    private final String groupId;
    private final String name;
    private final JsonNode payload;

    @JsonCreator
    public CreateDataRecord(@JsonProperty("groupId") String groupId,
                            @JsonProperty("name") String name,
                            @JsonProperty("payload") JsonNode payload) {
        this.groupId = groupId;
        this.name = name;
        this.payload = payload;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public JsonNode getPayload() {
        return payload;
    }

}
