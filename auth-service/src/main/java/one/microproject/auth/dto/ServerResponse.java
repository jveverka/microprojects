package one.microproject.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ServerResponse<T>(
        @JsonProperty("data") T data,
        @JsonProperty("error") ErrorData error
) {

    @JsonIgnore
    public boolean isSuccess() {
        return error == null;
    }

    @JsonIgnore
    public boolean isError() {
        return error != null;
    }

    public static ServerResponse<Void> ok() {
        return new ServerResponse<>(null, null);
    }

    public static <T> ServerResponse<T> ok(T data) {
        return new ServerResponse<>(data, null);
    }

    public static ServerResponse<Void> error(Integer code, String message) {
        return new ServerResponse<>(null, new ErrorData(code, message));
    }

}
