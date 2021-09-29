package com.toquery.framework.example.rest;

import com.google.common.collect.Lists;
import com.toquery.framework.example.entity.BizNews;
import com.toquery.framework.example.service.IBizNewsService;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.webmvc.domain.ResponsePage;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author toquery
 * @version 1
 */
@RestController
public class AppIndexRest extends AppBaseCrudController<IBizNewsService, BizNews> {


    @RequestMapping({"", "/", "/index"})
    public String index() {
        return "Hello World!";
    }


    @RequestMapping("/page")
    public ResponsePage page() {
        return new ResponsePage(12, 13, 14L, 2);
    }


    @RequestMapping("/response")
    public ResponseParam response() {
        return ResponseParam.builder().page(this.page()).content(Lists.newArrayList()).build();
    }

}
