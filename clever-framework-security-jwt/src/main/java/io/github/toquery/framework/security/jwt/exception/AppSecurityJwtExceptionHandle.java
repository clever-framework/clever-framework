package io.github.toquery.framework.security.jwt.exception;

/**
 * @author toquery
 * @version 1
 */

import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AppSecurityJwtExceptionHandle {


    @ResponseBody
    @ExceptionHandler(AppSecurityJwtException.class)
    public ResponseEntity<ResponseParam> handleAuthenticationException(AppSecurityJwtException e) {
        e.printStackTrace();
        return ResponseEntity.status(e.getHttpStatus()).body(ResponseParam.builder().build().message(e.getMessage()));
    }
}
