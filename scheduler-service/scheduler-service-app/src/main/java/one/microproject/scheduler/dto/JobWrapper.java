package one.microproject.scheduler.dto;

import java.util.concurrent.ScheduledFuture;

public class JobWrapper {

    private final ScheduledFuture<?> scheduledFuture;
    private final JobId id;
    private JobResult lastResult;

    public JobWrapper(ScheduledFuture<?> scheduledFuture, JobId id) {
        this.scheduledFuture = scheduledFuture;
        this.id = id;
    }

    public JobId getId() {
        return id;
    }

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public void setResult(JobResult result) {
        this.lastResult = result;
    }

    public JobResult getLastResult() {
        return lastResult;
    }

}
