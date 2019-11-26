package io.github.toquery.framework.minio.advice;

import io.github.toquery.framework.minio.exception.AppMinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author toquery
 * @version 1
 */
@Slf4j
@ControllerAdvice
public class AppMinioExceptionAdvice {

    public AppMinioExceptionAdvice() {
        log.info("初始化 App Minio 异常捕获");
    }

    @ResponseBody
    @ExceptionHandler(AppMinioException.class)
    public ResponseEntity handleAppException(AppMinioException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件存储服务错误！");
    }
}
