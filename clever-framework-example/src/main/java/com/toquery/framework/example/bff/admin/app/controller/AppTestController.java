package com.toquery.framework.example.bff.admin.app.controller;

import com.google.common.collect.Lists;
import com.toquery.framework.example.modules.news.info.entity.BizNews;
import com.toquery.framework.example.modules.news.info.service.BizNewsDomainService;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.webmvc.domain.ResponsePage;
import io.github.toquery.framework.webmvc.domain.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/admin/test")
public class AppTestController extends AppBaseCrudController<BizNewsDomainService, BizNews> {


    @GetMapping("/page")
    public ResponsePage pageResponse() {
        return new ResponsePage(12, 13, 14L, 2);
    }


    @GetMapping("/response")
    public ResponseResult response() {
        return ResponseResult.builder().page(this.page()).content(Lists.newArrayList()).build();
    }


}
