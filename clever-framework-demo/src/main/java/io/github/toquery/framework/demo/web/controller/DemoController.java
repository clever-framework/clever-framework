package io.github.toquery.framework.demo.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping({"/", "/index"})
    public String index() {
        return "OK";
    }

}
