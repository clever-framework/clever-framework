package com.toquery.framework.demo.rest;

import io.github.toquery.framework.webmvc.domain.ResponsePage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/page")
public class TestPageController {


    @RequestMapping("/page")
    public ResponsePage test() {
        return new ResponsePage(12, 13, 14L, 2);
    }

}
