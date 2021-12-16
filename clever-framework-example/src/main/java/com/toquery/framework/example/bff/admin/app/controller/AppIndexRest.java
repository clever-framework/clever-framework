package com.toquery.framework.example.bff.admin.app.controller;

import com.google.common.collect.Lists;
import com.toquery.framework.example.modules.news.info.entity.BizNews;
import com.toquery.framework.example.modules.news.info.service.BizNewsDomainService;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.webmvc.domain.ResponsePage;
import io.github.toquery.framework.webmvc.domain.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/admin")
public class AppIndexRest extends AppBaseCrudController<BizNewsDomainService, BizNews> {


    @RequestMapping({"", "/", "/index"})
    public String index() {
        return "Hello World!";
    }


    @RequestMapping("/page")
    public ResponsePage pageResponse() {
        return new ResponsePage(12, 13, 14L, 2);
    }


    @RequestMapping("/response")
    public ResponseResult response() {
        return ResponseResult.builder().page(this.page()).content(Lists.newArrayList()).build();
    }

}
