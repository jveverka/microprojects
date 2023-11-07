package one.microproject.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserAuthRequest(
        @JsonProperty("username")
        String username,
        @JsonProperty("password")
        String password) {
}
