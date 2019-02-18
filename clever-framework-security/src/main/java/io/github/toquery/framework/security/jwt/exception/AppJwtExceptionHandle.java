package io.github.toquery.framework.security.jwt.exception;

/**
 * @author toquery
 * @version 1
 */

import io.github.toquery.framework.web.domain.ResponseParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AppJwtExceptionHandle {


    @ResponseBody
    @ExceptionHandler(AppJwtException.class)
    public ResponseEntity<ResponseParam> handleAuthenticationException(AppJwtException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(ResponseParam.fail().message(e.getMessage()));
    }
}
