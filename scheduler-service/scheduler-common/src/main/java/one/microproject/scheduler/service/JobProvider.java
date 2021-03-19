package one.microproject.scheduler.service;

import com.fasterxml.jackson.databind.JsonNode;
import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.TaskInfo;

public interface JobProvider {

    TaskInfo getTaskInfo();

    JobInstance createJob(JobId jobId, JsonNode taskParameters) throws CreateJobException;

}
