package one.microproject.scheduler.exampletask;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExampleJobResult {

    private final String result;

    @JsonCreator
    public ExampleJobResult(@JsonProperty("result") String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

}
