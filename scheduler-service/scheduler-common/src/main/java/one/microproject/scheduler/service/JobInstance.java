package one.microproject.scheduler.service;

public interface JobInstance<R> {

    R getResult() throws JobException;

}
