package one.microproject.events.dto;

public record EventDTO(String streamId, String activityType, Long quantity, Long duration, String unit, String[] labels) {
}
