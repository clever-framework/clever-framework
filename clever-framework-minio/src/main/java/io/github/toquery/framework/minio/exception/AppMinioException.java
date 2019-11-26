package io.github.toquery.framework.minio.exception;

public class AppMinioException extends Exception {
    public AppMinioException() {
        super();
    }

    public AppMinioException(String message) {
        super(message);
    }

    public AppMinioException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppMinioException(Throwable cause) {
        super(cause);
    }

    protected AppMinioException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
