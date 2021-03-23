package one.microproject.scheduler.exampletask;

import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.service.JobException;
import one.microproject.scheduler.service.JobInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleJob implements JobInstance<ExampleJobResult> {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleJob.class);

    private final JobId jobId;
    private final ExampleJobParameters jobParameters;

    public ExampleJob(JobId jobId, ExampleJobParameters jobParameters) {
        this.jobId  = jobId;
        this.jobParameters = jobParameters;
        LOG.info("creating ExampleJob {}", jobId.getId());
    }

    @Override
    public ExampleJobResult getResult() throws JobException {
        try {
            LOG.info("Executing ExampleJob {}:{}", jobId.getId(), Thread.currentThread().getName());
            Thread.sleep(jobParameters.getDelay());
            return new ExampleJobResult("OK");
        } catch (InterruptedException e) {
            throw new JobException(e);
        }
    }

}
