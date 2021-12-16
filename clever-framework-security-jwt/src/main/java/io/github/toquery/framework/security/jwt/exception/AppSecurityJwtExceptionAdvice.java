package io.github.toquery.framework.security.jwt.exception;

/**
 * @author toquery
 * @version 1
 */

import io.github.toquery.framework.webmvc.domain.ResponseResult;
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


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @org.springframework.web.bind.annotation.ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseResult> handleExpiredJwtException(AccessDeniedException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseResult.builder().code(HttpStatus.UNAUTHORIZED.value()).success(false).message("访问被拒绝！").build());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @org.springframework.web.bind.annotation.ResponseBody
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ResponseResult> handleExpiredJwtException(ExpiredJwtException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseResult.builder().code(HttpStatus.UNAUTHORIZED.value()).success(false).message("登录失效！").build());
    }

    @org.springframework.web.bind.annotation.ResponseBody
    @ExceptionHandler(AppSecurityJwtException.class)
    public ResponseEntity<ResponseResult> handleAuthenticationException(AppSecurityJwtException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(exception.getHttpStatus()).body(ResponseResult.builder().code(exception.getHttpStatus().value()).success(false).message(exception.getMessage()).build());
    }
}
