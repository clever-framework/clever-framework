package com.toquery.framework.example.bff.admin.app.controller;

import com.google.common.collect.Lists;
import com.toquery.framework.example.bff.admin.app.model.TestResponseWrap;
import io.github.toquery.framework.common.util.AesCbcUtil;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.webmvc.annotation.ResponseIgnoreWrap;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.github.toquery.framework.webmvc.properties.AppWebMvcProperties;
import io.github.toquery.framework.webmvc.properties.AppWebMvcSecretProperties;
import io.github.toquery.framework.webmvc.secret.annotation.ResponseIgnoreSecret;
import io.github.toquery.framework.webmvc.secret.annotation.ResponseSecret;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 */
@Slf4j
@RestController
@RequestMapping("/admin/test")
public class AppTestSecretBodyController {

    @Autowired
    private AppWebMvcProperties appWebMvcProperties;

    @ResponseIgnoreSecret
    @GetMapping("/secret-encode")
    public ResponseBodyWrap secretEncode(@RequestParam String requestBody) {
        AppWebMvcSecretProperties.AppWebMvcSecretItem secret = appWebMvcProperties.getSecret().getResponse();
        return ResponseBodyWrap.builder().content(AesCbcUtil.encode(requestBody, secret.getKey(), secret.getIv())).build();
    }

    @ResponseIgnoreSecret
    @GetMapping("/secret-decode")
    public ResponseBodyWrap secretDecode(@RequestParam String requestBody) {
        AppWebMvcSecretProperties.AppWebMvcSecretItem secret = appWebMvcProperties.getSecret().getResponse();
        return ResponseBodyWrap.builder().content(AesCbcUtil.decode(requestBody, secret.getKey(), secret.getIv())).build();
    }

    @ResponseSecret
    @GetMapping("/secret-void")
    public void secretVoid() {
        log.info("将返回 secret void");
    }

    @ResponseSecret
    @ResponseIgnoreWrap
    @GetMapping("/secret-void-ignore")
    public void secretVoidIgnore() {
        log.info("将返回 secret void");
    }


    @ResponseSecret
    @GetMapping("/secret-string")
    public ResponseBodyWrap secretString() {
        return ResponseBodyWrap.builder().content("内容将加密处理").build();
    }

    @ResponseSecret
    @GetMapping(value = "/secret-string-wrap")
    public TestResponseWrap secretStringWrap() {
        return new TestResponseWrap("内容将加密处理并包裹");
    }

    @ResponseSecret
    @ResponseIgnoreWrap
    @GetMapping("/secret-string-wrap-ignore")
    public TestResponseWrap secretStringIgnoreWrap() {
        return new TestResponseWrap("内容将加密处理");
    }


    @ResponseSecret
    @GetMapping("/secret-list")
    public ResponseBodyWrap secretList() {
        return ResponseBodyWrap.builder().content(this.secretListWrap()).build();
    }

    @ResponseSecret
    @GetMapping("/secret-list-wrap")
    public List<SysUser> secretListWrap() {
        List<SysUser> sysUserList = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            SysUser sysUser = new SysUser();
            sysUser.setUsername("username" + i);
            sysUser.setPassword("password" + i);
            sysUser.setId(Long.parseLong(i + ""));
            sysUserList.add(sysUser);
        }
        return sysUserList;
    }
}
