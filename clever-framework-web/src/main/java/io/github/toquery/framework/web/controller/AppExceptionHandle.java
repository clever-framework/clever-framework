package io.github.toquery.framework.web.controller;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.web.domain.ResponseParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@ControllerAdvice
public class AppExceptionHandle {

    public AppExceptionHandle() {
        log.info("初始化 App 异常捕获");
    }

    @ResponseBody
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseParam> handleAppException(AppException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseParam.builder().build().message(e.getMessage()));
    }


    /*
    @ResponseBody
    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<ResponseParam> handleRollbackException(RollbackException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseParam.builder().build().message(e.getMessage()));
    }
    */

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseParam> handleConstraintViolationException(ConstraintViolationException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseParam.builder().build().message(e.getConstraintViolations().iterator().next().getMessage()));
    }
}
