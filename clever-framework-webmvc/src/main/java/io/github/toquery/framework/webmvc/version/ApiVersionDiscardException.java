package io.github.toquery.framework.webmvc.version;

import io.github.toquery.framework.core.exception.AppException;

/**
 * @author toquery
 * @since 2019/1/1
 */
public class ApiVersionDiscardException extends AppException {
   /**
     *
     */
    private static final long serialVersionUID = -5532680226339212745L;

 public ApiVersionDiscardException(String message) {
        super(message);
    }
}
