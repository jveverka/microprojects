package one.microproject.scheduler.exampletask;

import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.service.JobResultCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleJob implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleJob.class);

    private final JobId jobId;
    private final ExampleJobParameters jobParameters;
    private final JobResultCache jobResultCache;
    private final ObjectMapper mapper;

    public ExampleJob(JobId jobId, ExampleJobParameters jobParameters,
                      JobResultCache jobResultCache, ObjectMapper mapper) {
        this.jobId  = jobId;
        this.jobParameters = jobParameters;
        this.jobResultCache = jobResultCache;
        this.mapper = mapper;
        LOG.info("creating ExampleJob {}", jobId.getId());
    }

    @Override
    public void run() {
        LOG.info("Executing ExampleJob {}:{}", jobId.getId(), Thread.currentThread().getName());
    }

}
