package io.github.toquery.framework.security.jwt.exception;

/**
 * @author toquery
 * @version 1
 */
public class AppJwtException extends Exception {
    public AppJwtException() {
    }

    public AppJwtException(String message) {
        super(message);
    }

    public AppJwtException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppJwtException(Throwable cause) {
        super(cause);
    }

    public AppJwtException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
