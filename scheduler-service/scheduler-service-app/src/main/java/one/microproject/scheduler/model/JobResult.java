package one.microproject.scheduler.model;

import com.fasterxml.jackson.databind.JsonNode;
import one.microproject.scheduler.dto.JobStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class JobResult {

    @Id
    private String id;
    private String taskType;
    private Long startedTimeStamp;
    private Long duration;
    private JobStatus status;
    private String result;

    public JobResult() {
    }

    public JobResult(String id, String taskType, Long startedTimeStamp, Long duration, JobStatus status, String result) {
        this.id = id;
        this.taskType = taskType;
        this.startedTimeStamp = startedTimeStamp;
        this.duration = duration;
        this.status = status;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Long getStartedTimeStamp() {
        return startedTimeStamp;
    }

    public void setStartedTimeStamp(Long startedTimeStamp) {
        this.startedTimeStamp = startedTimeStamp;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
