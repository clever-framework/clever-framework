package io.github.toquery.framework.security.jwt.exception;

/**
 * @author toquery
 * @version 1
 */

import io.github.toquery.framework.webmvc.domain.ResponseParam;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Slf4j
@ControllerAdvice
public class AppSecurityJwtExceptionHandle {

    public AppSecurityJwtExceptionHandle(){
        log.info("初始化 App Security Jwt Exception Handle");
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseParam> handleExpiredJwtException(AccessDeniedException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseParam.builder().message("访问被拒绝！").build());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ResponseParam> handleExpiredJwtException(ExpiredJwtException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseParam.builder().message("登录失效！").build());
    }

    @ResponseBody
    @ExceptionHandler(AppSecurityJwtException.class)
    public ResponseEntity<ResponseParam> handleAuthenticationException(AppSecurityJwtException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(exception.getHttpStatus()).body(ResponseParam.builder().message(exception.getMessage()).build());
    }
}
