package one.microproject.scheduler.exampletask;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.service.CreateJobException;
import one.microproject.scheduler.service.JobProvider;
import one.microproject.scheduler.service.JobResultCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JobProviderImpl implements JobProvider {

    private static final Logger LOG = LoggerFactory.getLogger(JobProviderImpl.class);

    public JobProviderImpl() {
        LOG.info("init ...");
    }

    @Override
    public String getTaskType() {
        return "example-task";
    }

    @Override
    public Runnable createJob(JobId jobId, JsonNode taskParameters, JobResultCache jobResultCache) throws CreateJobException {
        try {
            LOG.info("createJob {}", jobId.getId());
            ObjectMapper mapper = new ObjectMapper();
            ExampleJobParameters exampleJobParameters = mapper.treeToValue(taskParameters, ExampleJobParameters.class);
            return new ExampleJob(jobId, exampleJobParameters, jobResultCache, mapper);
        } catch (IOException e) {
            throw new CreateJobException(e);
        }
    }

}
