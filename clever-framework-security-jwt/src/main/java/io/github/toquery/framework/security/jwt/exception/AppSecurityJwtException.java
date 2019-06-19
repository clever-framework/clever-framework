package io.github.toquery.framework.security.jwt.exception;

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
public class AppSecurityJwtException extends AppException {

    private HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public AppSecurityJwtException() {
    }

    public AppSecurityJwtException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }


    public AppSecurityJwtException(String message) {
        super(message);
    }

    public AppSecurityJwtException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppSecurityJwtException(Throwable cause) {
        super(cause);
    }

    public AppSecurityJwtException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
