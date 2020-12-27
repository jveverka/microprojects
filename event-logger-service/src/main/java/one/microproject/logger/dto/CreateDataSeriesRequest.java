package one.microproject.logger.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateDataSeriesRequest {

    private final String groupId;
    private final String name;
    private final String description;

    @JsonCreator
    public CreateDataSeriesRequest(@JsonProperty("groupId") String groupId,
                                   @JsonProperty("name") String name,
                                   @JsonProperty("description") String description) {
        this.groupId = groupId;
        this.name = name;
        this.description = description;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
