package com.toquery.framework.demo.rest;

import com.toquery.framework.demo.entity.BizNews;
import com.toquery.framework.demo.service.IBizNewsService;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/demo")
public class DemoController extends AppBaseCrudController<IBizNewsService, BizNews,Long> {

    @Resource
    private IBizNewsService demoService;


    @RequestMapping({"/", "/index"})
    public String index() {
        return "OK";
    }

}
