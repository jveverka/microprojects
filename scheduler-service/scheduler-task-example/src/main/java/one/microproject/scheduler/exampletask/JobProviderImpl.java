package one.microproject.scheduler.exampletask;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.TaskInfo;
import one.microproject.scheduler.service.CreateJobException;
import one.microproject.scheduler.service.JobProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JobProviderImpl implements JobProvider {

    private static final Logger LOG = LoggerFactory.getLogger(JobProviderImpl.class);

    public JobProviderImpl() {
        LOG.info("init ...");
    }

    @Override
    public TaskInfo getTaskInfo() {
        return new TaskInfo("example-task",  "Example Task");
    }

    @Override
    public ExampleJob createJob(JobId jobId, JsonNode taskParameters) throws CreateJobException {
        try {
            LOG.info("createJob {}", jobId.getId());
            ObjectMapper mapper = new ObjectMapper();
            ExampleJobParameters exampleJobParameters = mapper.treeToValue(taskParameters, ExampleJobParameters.class);
            return new ExampleJob(jobId, exampleJobParameters, mapper);
        } catch (IOException e) {
            throw new CreateJobException(e);
        }
    }

}
