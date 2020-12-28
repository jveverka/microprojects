package one.microproject.logger.tests.testdto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DataWrapper {

    private final Integer value;

    @JsonCreator
    public DataWrapper(@JsonProperty("value") Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
