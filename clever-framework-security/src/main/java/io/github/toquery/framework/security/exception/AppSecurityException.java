package io.github.toquery.framework.security.exception;

import io.github.toquery.framework.core.exception.AppException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author toquery
 * @version 1
 */
@Getter
@Setter
public class AppSecurityException extends AppException {

    private HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public AppSecurityException() {
    }

    public AppSecurityException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }


    public AppSecurityException(String message) {
        super(message);
    }

    public AppSecurityException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public AppSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppSecurityException(String message, Throwable cause, HttpStatus httpStatus) {
        this(message, cause);
        this.httpStatus = httpStatus;
    }

    public AppSecurityException(Throwable cause) {
        super(cause);
    }

    public AppSecurityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
