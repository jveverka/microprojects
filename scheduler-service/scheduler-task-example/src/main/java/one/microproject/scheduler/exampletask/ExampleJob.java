package one.microproject.scheduler.exampletask;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.service.JobException;
import one.microproject.scheduler.service.JobInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleJob implements JobInstance {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleJob.class);

    private final JobId jobId;
    private final ExampleJobParameters jobParameters;
    private final ObjectMapper mapper;

    public ExampleJob(JobId jobId, ExampleJobParameters jobParameters, ObjectMapper mapper) {
        this.jobId  = jobId;
        this.jobParameters = jobParameters;
        this.mapper = mapper;
        LOG.info("creating ExampleJob {}", jobId.getId());
    }

    /**
    @Override
    public void run() {
        Long timeStamp = System.currentTimeMillis();
        try {
            LOG.info("Executing ExampleJob {}:{}", jobId.getId(), Thread.currentThread().getName());
            Thread.sleep(jobParameters.getDelay());
            ExampleJobResult result = new ExampleJobResult("OK");
            Long duration = System.currentTimeMillis() - timeStamp;
            jobResultCache.setResult(jobId, timeStamp, duration, mapper.valueToTree(result));
            LOG.info("Done {}:{}", jobId.getId(), Thread.currentThread().getName());
        } catch (InterruptedException e) {
            LOG.error("Error: ", e);
            ExampleJobResult result = new ExampleJobResult("ERROR: " + e.getMessage());
            Long duration = System.currentTimeMillis() - timeStamp;
            jobResultCache.setResult(jobId, timeStamp, duration, mapper.valueToTree(result));
        }
    }*/

    @Override
    public JsonNode getResult() throws JobException {
        try {
            LOG.info("Executing ExampleJob {}:{}", jobId.getId(), Thread.currentThread().getName());
            Thread.sleep(jobParameters.getDelay());
            ExampleJobResult result = new ExampleJobResult("OK");
            return mapper.valueToTree(result);
        } catch (InterruptedException e) {
            throw new JobException(e);
        }
    }

}
