package one.microproject.scheduler.service;

import com.fasterxml.jackson.databind.JsonNode;
import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobHandler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(JobHandler.class);

    private final JobId jobId;
    private final JobInstance jobInstance;
    private final JobResultCache jobResultCache;

    public JobHandler(JobId jobId, JobInstance jobInstance, JobResultCache jobResultCache) {
        this.jobId = jobId;
        this.jobInstance = jobInstance;
        this.jobResultCache = jobResultCache;
    }

    @Override
    public void run() {
        Long timeStamp = System.currentTimeMillis();
        try {
            LOG.info("Executing ExampleJob {}:{}", jobId.getId(), Thread.currentThread().getName());
            JsonNode result = jobInstance.getResult();
            Long duration = System.currentTimeMillis() - timeStamp;
            jobResultCache.setResult(jobId, timeStamp, duration, ResultCode.OK, result);
            LOG.info("Done {}:{}", jobId.getId(), Thread.currentThread().getName());
        } catch (Exception e) {
            LOG.error("Error: ", e);
            Long duration = System.currentTimeMillis() - timeStamp;
            jobResultCache.setResult(jobId, timeStamp, duration, ResultCode.OK, null);
        }
    }

}
