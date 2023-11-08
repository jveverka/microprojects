package one.microproject.auth.exceptions;

public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable t) {
        super(message, t);
    }

}
