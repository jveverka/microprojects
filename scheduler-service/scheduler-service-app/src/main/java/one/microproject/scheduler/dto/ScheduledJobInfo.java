package one.microproject.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.TimeUnit;

public class ScheduledJobInfo {

    private final JobId jobId;
    private final String taskType;
    private final Long interval;
    private final TimeUnit timeUnit;
    private final JobResult lastResult;

    @JsonCreator
    public ScheduledJobInfo(@JsonProperty("jobId") JobId jobId,
                            @JsonProperty("taskType") String taskType,
                            @JsonProperty("interval") Long interval,
                            @JsonProperty("timeUnit") TimeUnit timeUnit,
                            @JsonProperty("lastResult") JobResult lastResult) {
        this.jobId = jobId;
        this.taskType = taskType;
        this.interval = interval;
        this.timeUnit = timeUnit;
        this.lastResult = lastResult;
    }

    public JobId getJobId() {
        return jobId;
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

    public JobResult getLastResult() {
        return lastResult;
    }

}
