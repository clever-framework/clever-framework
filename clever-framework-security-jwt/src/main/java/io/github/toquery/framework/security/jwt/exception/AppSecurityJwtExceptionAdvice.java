package io.github.toquery.framework.security.jwt.exception;

/**
 * @author toquery
 * @version 1
 */

import io.github.toquery.framework.web.domain.ResponseBody;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
//@ControllerAdvice
@RestControllerAdvice
public class AppSecurityJwtExceptionAdvice {

    public AppSecurityJwtExceptionAdvice(){
        log.info("初始化 App Security Jwt Exception Handle");
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @org.springframework.web.bind.annotation.ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleExpiredJwtException(AccessDeniedException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseBody.builder().code(HttpStatus.FORBIDDEN.value()).fail("访问被拒绝！").build());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @org.springframework.web.bind.annotation.ResponseBody
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseBody.builder().code(HttpStatus.UNAUTHORIZED.value()).fail("登录失效！").build());
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @org.springframework.web.bind.annotation.ResponseBody
    @ExceptionHandler(AppSecurityJwtException.class)
    public ResponseEntity<?> handleAuthenticationException(AppSecurityJwtException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(exception.getHttpStatus()).body(ResponseBody.builder().code(exception.getHttpStatus().value()).fail(exception.getMessage()).build());
    }
}
