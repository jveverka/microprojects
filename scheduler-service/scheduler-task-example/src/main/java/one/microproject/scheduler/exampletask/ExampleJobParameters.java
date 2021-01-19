package one.microproject.scheduler.exampletask;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExampleJobParameters {

    private final Long delay;

    @JsonCreator
    public ExampleJobParameters(@JsonProperty("delay") Long delay) {
        this.delay = delay;
    }

    public Long getDelay() {
        return delay;
    }

}
