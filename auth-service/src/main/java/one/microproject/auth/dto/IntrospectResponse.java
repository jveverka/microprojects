package one.microproject.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IntrospectResponse(@JsonProperty("active") Boolean active) {
}
