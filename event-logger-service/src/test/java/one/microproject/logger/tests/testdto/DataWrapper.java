package one.microproject.logger.tests.testdto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class DataWrapper {

    private final Integer value;
    private final Boolean result;
    private final String data;
    private final DataWrapper wrapper;

    @JsonCreator
    public DataWrapper(@JsonProperty("value") Integer value,
                       @JsonProperty("result") Boolean result,
                       @JsonProperty("data") String data,
                       @JsonProperty("wrapper") DataWrapper wrapper) {
        this.value = value;
        this.result = result;
        this.data = data;
        this.wrapper = wrapper;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean getResult() {
        return result;
    }

    public String getData() {
        return data;
    }

    public DataWrapper getWrapper() {
        return wrapper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataWrapper that = (DataWrapper) o;
        return Objects.equals(value, that.value) && Objects.equals(result, that.result) && Objects.equals(data, that.data) && Objects.equals(wrapper, that.wrapper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, result, data, wrapper);
    }

    public static DataWrapper getInstance() {
        return new DataWrapper(1, true, "test", null);
    }

}
