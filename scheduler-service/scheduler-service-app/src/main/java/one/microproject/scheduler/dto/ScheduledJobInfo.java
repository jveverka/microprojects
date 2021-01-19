package one.microproject.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.TimeUnit;

public class ScheduledJobInfo {

    private final JobId jobId;
    private final String taskType;
    private final String name;
    private final Long interval;
    private final TimeUnit timeUnit;
    private final JobResult lastResult;

    @JsonCreator
    public ScheduledJobInfo(@JsonProperty("jobId") JobId jobId,
                            @JsonProperty("taskType") String taskType,
                            @JsonProperty("name") String name,
                            @JsonProperty("interval") Long interval,
                            @JsonProperty("timeUnit") TimeUnit timeUnit,
                            @JsonProperty("lastResult") JobResult lastResult) {
        this.jobId = jobId;
        this.taskType = taskType;
        this.name = name;
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

    public String getName() {
        return name;
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

    public static ScheduledJobInfo from(ScheduledJobInfo scheduledJobInfo, JobResult lastResult) {
        return new ScheduledJobInfo(
                scheduledJobInfo.jobId,
                scheduledJobInfo.taskType,
                scheduledJobInfo.name,
                scheduledJobInfo.interval,
                scheduledJobInfo.timeUnit,
                lastResult);
    }

}
