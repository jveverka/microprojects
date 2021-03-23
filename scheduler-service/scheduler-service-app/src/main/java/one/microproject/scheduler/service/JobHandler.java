package one.microproject.scheduler.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobHandler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(JobHandler.class);

    private final JobId jobId;
    private final JobInstance<?> jobInstance;
    private final JobResultCache jobResultCache;
    private final ObjectMapper mapper;

    public JobHandler(JobId jobId, JobInstance<?> jobInstance, JobResultCache jobResultCache) {
        this.jobId = jobId;
        this.jobInstance = jobInstance;
        this.jobResultCache = jobResultCache;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void run() {
        Long timeStamp = System.currentTimeMillis();
        try {
            LOG.info("Executing ExampleJob {}:{}", jobId.getId(), Thread.currentThread().getName());
            Object result = jobInstance.getResult();
            JsonNode jsonResult = mapper.valueToTree(result);
            Long duration = System.currentTimeMillis() - timeStamp;
            jobResultCache.setResult(jobId, timeStamp, duration, ResultCode.OK, jsonResult);
            LOG.info("Done {}:{}", jobId.getId(), Thread.currentThread().getName());
        } catch (Exception e) {
            LOG.error("Error: ", e);
            Long duration = System.currentTimeMillis() - timeStamp;
            jobResultCache.setResult(jobId, timeStamp, duration, ResultCode.OK, null);
        }
    }

}
