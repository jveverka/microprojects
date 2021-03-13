package one.microproject.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.TimeUnit;

public class ScheduleJobRequest {

    private final String taskType;
    private final Long startDate;
    private final Long interval;
    private final TimeUnit timeUnit;
    private final Long repeat;
    private final JsonNode taskParameters;

    @JsonCreator
    public ScheduleJobRequest(@JsonProperty("taskType") String taskType,
                              @JsonProperty("startDate") Long startDate,
                              @JsonProperty("interval") Long interval,
                              @JsonProperty("timeUnit") TimeUnit timeUnit,
                              @JsonProperty("repeat") Long repeat,
                              @JsonProperty("taskParameters") JsonNode taskParameters) {
        this.taskType = taskType;
        this.startDate =  startDate;
        this.interval = interval;
        this.timeUnit = timeUnit;
        this.repeat = repeat;
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

    public Long getStartDate() {
        return startDate;
    }

    public Long getRepeat() {
        return repeat;
    }

}
