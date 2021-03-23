package one.microproject.scheduler.exampletask;

import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.TaskInfo;
import one.microproject.scheduler.service.CreateJobException;
import one.microproject.scheduler.service.JobInstance;
import one.microproject.scheduler.service.JobProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JobProviderImpl implements JobProvider<ExampleJobParameters, ExampleJobResult> {

    private static final Logger LOG = LoggerFactory.getLogger(JobProviderImpl.class);

    public JobProviderImpl() {
        LOG.info("init ...");
    }

    @Override
    public TaskInfo getTaskInfo() {
        return new TaskInfo("example-task",  "Example Task");
    }

    @Override
    public JobInstance<ExampleJobResult> createJob(JobId jobId, ExampleJobParameters taskParameters) throws CreateJobException {
        try {
            LOG.info("createJob {}", jobId.getId());
            return new ExampleJob(jobId, taskParameters);
        } catch (RuntimeException e) {
            throw new CreateJobException(e);
        }
    }

    @Override
    public Class<ExampleJobParameters> getTaskParametersType() {
        return ExampleJobParameters.class;
    }

}
