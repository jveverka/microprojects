package one.microproject.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorData(@JsonProperty("code") Integer code, @JsonProperty("message") String message) {

    public static ErrorData of(Integer code, String message) {
        return new ErrorData(code, message);
    }

}
