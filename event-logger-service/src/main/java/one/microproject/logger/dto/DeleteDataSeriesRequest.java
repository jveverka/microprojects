package one.microproject.logger.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteDataSeriesRequest {

    private final String groupId;
    private final String name;

    @JsonCreator
    public DeleteDataSeriesRequest(@JsonProperty("groupId") String groupId,
                                   @JsonProperty("name") String name) {
        this.groupId = groupId;
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

}
