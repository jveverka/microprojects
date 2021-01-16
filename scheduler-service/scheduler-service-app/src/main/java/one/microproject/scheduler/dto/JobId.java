package one.microproject.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobId {

    private final String id;

    @JsonCreator
    public JobId(@JsonProperty("id") String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static JobId from(String id) {
        return new JobId(id);
    }

    @Override
    public String toString() {
        return "JobId{" +
                "id='" + id + '\'' +
                '}';
    }
}
