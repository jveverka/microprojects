package one.microproject.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(value = AuthException.class)
    public ResponseEntity<Void> handleAuthException(final AuthException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
