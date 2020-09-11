package io.github.toquery.framework.webmvc.version;

/**
 * @author toquery
 * @since 2019/1/1
 */
public class ApiVersionDiscardException extends RuntimeException {
    public ApiVersionDiscardException(String message) {
        super(message);
    }
}
