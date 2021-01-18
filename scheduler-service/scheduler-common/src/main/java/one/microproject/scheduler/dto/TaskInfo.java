package one.microproject.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskInfo {

    private final String taskType;
    private final String name;

    @JsonCreator
    public TaskInfo(@JsonProperty("taskType") String taskType,
                    @JsonProperty("name") String name) {
        this.taskType = taskType;
        this.name = name;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getName() {
        return name;
    }

}
