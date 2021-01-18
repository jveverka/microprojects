package one.microproject.scheduler.service;

public class CreateJobException extends Exception {

    public CreateJobException() {
    }

    public CreateJobException(Throwable t) {
        super(t);
    }

    public CreateJobException(String message, Throwable t) {
        super(message, t);
    }

    public CreateJobException(String message) {
        super(message);
    }

}
