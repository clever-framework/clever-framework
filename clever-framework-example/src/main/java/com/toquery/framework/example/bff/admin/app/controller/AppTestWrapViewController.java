package com.toquery.framework.example.bff.admin.app.controller;

import io.github.toquery.framework.webmvc.secret.annotation.ResponseIgnoreSecret;
import io.github.toquery.framework.webmvc.secret.annotation.ResponseSecret;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
@Slf4j
@ResponseIgnoreSecret
@Controller
@RequestMapping("/admin/test")
public class AppTestWrapViewController {


    @ResponseSecret
    @GetMapping("/wrap-view")
    public String wrapViewSecret() {
        return "test/wrap-view";
    }


    @ResponseSecret
    @GetMapping("/wrap-model-view")
    public ModelAndView wrapModelView() {
        return new ModelAndView("test/wrap-view");
    }
}
