package com.toquery.framework.example.bff.admin.app.controller;

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
@Controller
@RequestMapping("/admin/test")
public class AppTestSecretBodyViewController {

    @ResponseSecret
    @GetMapping("/secret-view")
    public String secretView() {
        return "test/secret-view";
    }

    @ResponseSecret
    @GetMapping("/response-view-secret")
    public ModelAndView secretModelAndView() {
        return new ModelAndView("test/secret-view");
    }

}
