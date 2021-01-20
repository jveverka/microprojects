package one.microproject.scheduler.repository;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.concurrent.TimeUnit;

@Document
public class ScheduledJob {

    @Id
    private String id;

    private String taskType;
    private String name;
    private Long interval;
    private TimeUnit timeUnit;
    private String taskParameters;

    public ScheduledJob() {
    }

    public ScheduledJob(String id, String taskType, String name, Long interval, TimeUnit timeUnit, String taskParameters) {
        this.id = id;
        this.taskType = taskType;
        this.name = name;
        this.interval = interval;
        this.timeUnit = timeUnit;
        this.taskParameters = taskParameters;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getTaskParameters() {
        return taskParameters;
    }

    public void setTaskParameters(String taskParameters) {
        this.taskParameters = taskParameters;
    }

}
