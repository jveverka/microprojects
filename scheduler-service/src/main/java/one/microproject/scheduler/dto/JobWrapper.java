package one.microproject.scheduler.dto;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

public class JobWrapper {

    private final ScheduledFuture<?> scheduledFuture;
    private final JobResult result;

    public JobWrapper(ScheduledFuture<?> scheduledFuture, JobResult result) {
        this.scheduledFuture = scheduledFuture;
        this.result = result;
    }

    public JobWrapper(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
        this.result = null;
    }

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public Optional<JobResult> getResult() {
        return Optional.ofNullable(result);
    }

}
