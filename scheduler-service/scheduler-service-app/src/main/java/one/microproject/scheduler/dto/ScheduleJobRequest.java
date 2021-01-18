package one.microproject.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.TimeUnit;

public class ScheduleJobRequest {

    private final String taskType;
    private final Long interval;
    private final TimeUnit timeUnit;
    private final JsonNode taskParameters;

    @JsonCreator
    public ScheduleJobRequest(@JsonProperty("taskType") String taskType,
                              @JsonProperty("interval") Long interval,
                              @JsonProperty("timeUnit") TimeUnit timeUnit,
                              @JsonProperty("taskParameters") JsonNode taskParameters) {
        this.taskType = taskType;
        this.interval = interval;
        this.timeUnit = timeUnit;
        this.taskParameters = taskParameters;
    }

    public String getTaskType() {
        return taskType;
    }

    public Long getInterval() {
        return interval;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public JsonNode getTaskParameters() {
        return taskParameters;
    }

}
