package one.microproject.scheduler.service;

import com.fasterxml.jackson.databind.JsonNode;
import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.ResultCode;

public interface JobResultCache {

    void setResult(JobId jobId, Long startedTimeStamp, Long duration, ResultCode code, JsonNode result);

}
