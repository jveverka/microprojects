package one.microproject.scheduler.service;

import com.fasterxml.jackson.databind.JsonNode;
import one.microproject.scheduler.dto.JobId;

public interface JobResultCache {

    void setResult(JobId jobId, JsonNode result);

}
