package io.github.toquery.framework.webmvc.secret;

import java.io.IOException;

/**
 */
public class AppWebMvcSecretException extends IOException {

    public AppWebMvcSecretException() {
        super();
    }

    public AppWebMvcSecretException(String message) {
        super(message);
    }

    public AppWebMvcSecretException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppWebMvcSecretException(Throwable cause) {
        super(cause);
    }
}
