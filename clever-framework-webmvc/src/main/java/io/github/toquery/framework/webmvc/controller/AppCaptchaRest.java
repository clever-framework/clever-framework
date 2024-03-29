package io.github.toquery.framework.webmvc.controller;

import io.github.toquery.framework.webmvc.domain.CaptchaValidRequest;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.github.toquery.framework.webmvc.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping
public class AppCaptchaRest {

    @Autowired
    protected CaptchaService captchaService;

    /**
     * 获取图形验证码
     *
     * @return httpResponse
     */
    @GetMapping("/captcha")
    public ResponseBodyWrap captcha() {
        return ResponseBodyWrap.builder().content(captchaService.generateCaptcha()).build();
    }

    /**
     * 验证图形验证码
     *
     * @return httpResponse
     */
    @PostMapping("/captcha/valid")
    public ResponseBodyWrap captcha(@Validated @RequestBody CaptchaValidRequest captchaValidReq) {
        ResponseBodyWrap responseParam = ResponseBodyWrap.builder().success().build();
        if (!captchaService.valid(captchaValidReq.getCaptchaValue(), captchaValidReq.getCaptchaToken())) {
            responseParam = ResponseBodyWrap.builder().fail().message("验证码输入错误").build();
        }
        return responseParam;
    }
}
