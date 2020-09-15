package io.github.toquery.framework.front.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/front")
public class AppFrontConfigRest {

    @GetMapping("/config")
    public void frontConfig(){

    }
}
