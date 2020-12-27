package one.microproject.logger.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericResponse {

    private final Boolean ok;

    @JsonCreator
    public GenericResponse(@JsonProperty("ok") Boolean ok) {
        this.ok = ok;
    }

    public Boolean getOk() {
        return ok;
    }

    public static GenericResponse ok() {
        return new GenericResponse(true);
    }

    public static GenericResponse failed() {
        return new GenericResponse(false);
    }

}
