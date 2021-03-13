package one.microproject.scheduler.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.concurrent.TimeUnit;

@Document
public class ScheduledJob {

    @Id
    private String id;

    private String taskType;
    private Long startDate;
    private String name;
    private Long interval;
    private TimeUnit timeUnit;
    private Long repeat;
    private Long counter;
    private String taskParameters;

    public ScheduledJob() {
    }

    public ScheduledJob(String id, String taskType, Long startDate, String name, Long interval, TimeUnit timeUnit, Long repeat, Long counter, String taskParameters) {
        this.id = id;
        this.taskType = taskType;
        this.startDate = startDate;
        this.name = name;
        this.interval = interval;
        this.timeUnit = timeUnit;
        this.repeat = repeat;
        this.counter = counter;
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

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getRepeat() {
        return repeat;
    }

    public void setRepeat(Long repeat) {
        this.repeat = repeat;
    }

    public Long getCounter() {
        return counter;
    }

    public void setCounter(Long counter) {
        this.counter = counter;
    }

}
