package one.microproject.scheduler.service;

import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.TaskInfo;

public interface JobProvider<T, R> {

    TaskInfo getTaskInfo();

    JobInstance<R> createJob(JobId jobId, T taskParameters) throws CreateJobException;

    Class<T> getTaskParametersType();

}
