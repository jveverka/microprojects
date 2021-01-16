package one.microproject.scheduler.exampletask;

import com.fasterxml.jackson.databind.JsonNode;
import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.service.JobProvider;
import one.microproject.scheduler.service.JobResultCache;

public class JobProviderImpl implements JobProvider {

    @Override
    public String getTaskType() {
        return "example-task";
    }

    @Override
    public Runnable createJob(JobId jobId, JsonNode taskParameters, JobResultCache jobResultCache) {
        return null;
    }

}
