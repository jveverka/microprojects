package one.microproject.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class JobResult {

    private final JobId jobId;
    private final String taskType;
    private final String name;
    private final Long startedTimeStamp;
    private final Long duration;
    private final JsonNode result;

    @JsonCreator
    public JobResult(@JsonProperty("jobId") JobId jobId,
                     @JsonProperty("taskType") String taskType,
                     @JsonProperty("name") String name,
                     @JsonProperty("startedTimeStamp") Long startedTimeStamp,
                     @JsonProperty("duration") Long duration,
                     @JsonProperty("result") JsonNode result) {
        this.jobId = jobId;
        this.taskType = taskType;
        this.name = name;
        this.startedTimeStamp = startedTimeStamp;
        this.duration = duration;
        this.result = result;
    }

    public JobId getJobId() {
        return jobId;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getName() {
        return name;
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
