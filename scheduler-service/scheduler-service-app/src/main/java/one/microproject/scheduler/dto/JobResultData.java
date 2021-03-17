package one.microproject.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class JobResultData {

    private final Long startedTimeStamp;
    private final Long duration;
    private final JsonNode result;

    @JsonCreator
    public JobResultData(@JsonProperty("startedTimeStamp") Long startedTimeStamp,
                         @JsonProperty("duration") Long duration,
                         @JsonProperty("result") JsonNode result) {
        this.startedTimeStamp = startedTimeStamp;
        this.duration = duration;
        this.result = result;
    }

    public Long getStartedTimeStamp() {
        return startedTimeStamp;
    }

    public Long getDuration() {
        return duration;
    }

    public JsonNode getResult() {
        return result;
    }
}
