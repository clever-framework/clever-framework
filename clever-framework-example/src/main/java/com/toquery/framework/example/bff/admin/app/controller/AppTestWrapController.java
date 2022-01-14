package com.toquery.framework.example.bff.admin.app.controller;

import io.github.toquery.framework.webmvc.annotation.ResponseIgnoreWrap;
import io.github.toquery.framework.webmvc.domain.ResponseResult;
import io.github.toquery.framework.webmvc.secret.annotation.ResponseIgnoreSecret;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@Slf4j
@ResponseIgnoreSecret
@RestController
@RequestMapping("/admin/test")
public class AppTestWrapController {

    @GetMapping("/responseEntity")
    public ResponseEntity<?> responseEntity() {
        return ResponseEntity.ok(ResponseResult.builder().content("ResponseEntity").build());
    }

    @ResponseBody
    @GetMapping("/wrap")
    public String wrap() {
        return "将包裹响应";
    }

    @GetMapping("/wrap-void")
    public void wrapVoid() {
        log.info("将返回 wrap void");
    }

    @ResponseIgnoreWrap
    @GetMapping("/wrap-ignore")
    public String wrapIgnore() {
        return "将不包裹响应，直接返回String";
    }

}
