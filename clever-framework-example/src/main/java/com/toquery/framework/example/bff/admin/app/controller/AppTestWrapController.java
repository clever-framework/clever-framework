package com.toquery.framework.example.bff.admin.app.controller;

import com.toquery.framework.example.bff.admin.app.model.TestResponseWrap;
import io.github.toquery.framework.webmvc.annotation.ResponseIgnoreWrap;
import io.github.toquery.framework.web.domain.ResponseBody;
import io.github.toquery.framework.webmvc.secret.annotation.ResponseIgnoreSecret;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        return ResponseEntity.ok(ResponseBody.builder().content("ResponseEntity").build());
    }

    @org.springframework.web.bind.annotation.ResponseBody
    @GetMapping("/wrap")
    public TestResponseWrap wrap() {
        return new TestResponseWrap("将包裹响应");
    }

    @GetMapping("/wrap-void")
    public void wrapVoid() {
        log.info("将返回 wrap void");
    }

    @ResponseIgnoreWrap
    @GetMapping("/wrap-ignore")
    public TestResponseWrap wrapIgnore() {
        return new TestResponseWrap("将不包裹响应，直接返回String");
    }

}
