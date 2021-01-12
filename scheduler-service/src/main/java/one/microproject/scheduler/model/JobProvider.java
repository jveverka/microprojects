package one.microproject.scheduler.model;

import com.fasterxml.jackson.databind.JsonNode;
import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.service.JobResultCache;

public interface JobProvider {

    String getTaskType();

    Runnable createJob(JobId jobId, JsonNode taskParameters, JobResultCache jobResultCache);

}
