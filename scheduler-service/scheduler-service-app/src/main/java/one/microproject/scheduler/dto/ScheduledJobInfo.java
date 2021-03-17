package one.microproject.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.TimeUnit;

public class ScheduledJobInfo {

    private final JobId jobId;
    private final String taskType;
    private final Long startDate;
    private final String name;
    private final Long interval;
    private final Long repeat;
    private final Long counter;
    private final TimeUnit timeUnit;
    private final JobResultData lastResult;

    @JsonCreator
    public ScheduledJobInfo(@JsonProperty("jobId") JobId jobId,
                            @JsonProperty("taskType") String taskType,
                            @JsonProperty("startDate") Long startDate,
                            @JsonProperty("name") String name,
                            @JsonProperty("interval") Long interval,
                            @JsonProperty("repeat") Long repeat,
                            @JsonProperty("counter") Long counter,
                            @JsonProperty("timeUnit") TimeUnit timeUnit,
                            @JsonProperty("lastResult") JobResultData lastResult) {
        this.jobId = jobId;
        this.taskType = taskType;
        this.startDate = startDate;
        this.name = name;
        this.interval = interval;
        this.repeat = repeat;
        this.counter = counter;
        this.timeUnit = timeUnit;
        this.lastResult = lastResult;
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

    public Long getInterval() {
        return interval;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public JobResultData getLastResult() {
        return lastResult;
    }

    public Long getStartDate() {
        return startDate;
    }

    public Long getRepeat() {
        return repeat;
    }

    public Long getCounter() {
        return counter;
    }

}
