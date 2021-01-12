package one.microproject.scheduler.dto;

import java.util.concurrent.ScheduledFuture;

public class JobWrapper {

    private final ScheduledFuture<?> scheduledFuture;
    private final JobResult result;

    public JobWrapper(ScheduledFuture<?> scheduledFuture, JobResult result) {
        this.scheduledFuture = scheduledFuture;
        this.result = result;
    }

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public JobResult getResult() {
        return result;
    }

}
