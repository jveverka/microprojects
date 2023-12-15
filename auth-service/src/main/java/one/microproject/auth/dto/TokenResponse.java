package one.microproject.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse (
        @JsonProperty("access_token")
        String accessToken) {
}
