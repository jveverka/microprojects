package one.microproject.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class JobResultInfo {

    private final JobId id;
    private final String taskType;
    private final Long startedTimeStamp;
    private final Long duration;
    private final JobStatus status;
    private final ResultCode code;
    private final JsonNode result;

    @JsonCreator
    public JobResultInfo(@JsonProperty("id") JobId id,
                         @JsonProperty("taskType") String taskType,
                         @JsonProperty("startedTimeStamp") Long startedTimeStamp,
                         @JsonProperty("duration") Long duration,
                         @JsonProperty("status") JobStatus status,
                         @JsonProperty("code") ResultCode code,
                         @JsonProperty("result") JsonNode result) {
        this.id = id;
        this.taskType = taskType;
        this.startedTimeStamp = startedTimeStamp;
        this.duration = duration;
        this.status = status;
        this.code = code;
        this.result = result;
    }

    public JobId getId() {
        return id;
    }

    public String getTaskType() {
        return taskType;
    }

    public Long getStartedTimeStamp() {
        return startedTimeStamp;
    }

    public Long getDuration() {
        return duration;
    }

    public JobStatus getStatus() {
        return status;
    }

    public JsonNode getResult() {
        return result;
    }

    public ResultCode getCode() {
        return code;
    }

}
