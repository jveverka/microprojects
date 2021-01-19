package one.microproject.scheduler.dto;

import java.util.concurrent.ScheduledFuture;

public class JobWrapper {

    private final ScheduledFuture<?> scheduledFuture;
    private ScheduledJobInfo scheduledJobInfo;

    public JobWrapper(ScheduledFuture<?> scheduledFuture, ScheduledJobInfo scheduledJobInfo) {
        this.scheduledFuture = scheduledFuture;
        this.scheduledJobInfo = scheduledJobInfo;
    }

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public ScheduledJobInfo getScheduledJobInfo() {
        return scheduledJobInfo;
    }

    public void setResult(JobResult result) {
        this.scheduledJobInfo = ScheduledJobInfo.from(scheduledJobInfo, result);
    }

}
