package one.microproject.scheduler.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface JobInstance {

    JsonNode getResult() throws JobException;

}
