package one.microproject.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IntrospectRequest(@JsonProperty("token") String token) {
}
