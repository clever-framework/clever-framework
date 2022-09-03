package io.github.toquery.framework.security.exception;

/**
 * @author toquery
 * @version 1
 */

import com.nimbusds.jose.proc.BadJOSEException;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class AppSecurityExceptionAdvice {

    public AppSecurityExceptionAdvice() {
        log.info("初始化 App Security Jwt Exception Handle");
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleExpiredJwtException(AccessDeniedException exception) {
        log.error("handleExpiredJwtException", exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseBodyWrap.builder().code(HttpStatus.UNAUTHORIZED.value()).fail("访问被拒绝！").build());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(BadJOSEException.class)
    public ResponseEntity<?> handleBadJOSEException(BadJOSEException exception) {
        log.error("handleBadJOSEException", exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseBodyWrap.builder().code(HttpStatus.UNAUTHORIZED.value()).fail("登录失效！").build());
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(AppSecurityException.class)
    public ResponseEntity<?> handleAuthenticationException(AppSecurityException exception) {
        log.error("handleAuthenticationException", exception);
        return ResponseEntity.status(exception.getHttpStatus()).body(ResponseBodyWrap.builder().code(exception.getHttpStatus().value()).fail(exception.getMessage()).build());
    }
}
