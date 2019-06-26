package com.toquery.framework.demo.web.controller;

import com.toquery.framework.demo.entity.BizJpaNews;
import com.toquery.framework.demo.entity.BizBatisNews;
import com.toquery.framework.demo.service.IBizJpanewsService;
import com.toquery.framework.demo.service.IBizBatisNewsService;
import io.github.toquery.framework.webmvc.domain.ResponsePage;
import io.github.toquery.framework.webmvc.domain.ResponsePageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/page")
public class TestPageController {
    @Resource
    private IBizBatisNewsService myBatisDemoService;

    @RequestMapping("/page")
    public ResponsePage test() {
        return new ResponsePage(12, 13, 14, 2);
    }

    @RequestMapping("/mybatis")
    public Object mybatis(@RequestParam("name") String name,
                          @RequestParam(value = "page", defaultValue = "0") Integer page,
                          @RequestParam(value = "size", defaultValue = "15") Integer size) {
        com.github.pagehelper.Page<BizBatisNews> pageInfo = myBatisDemoService.findByName(name, page, size);
        return ResponsePageBuilder.build(pageInfo);
    }

    @Resource
    private IBizJpanewsService demoService;

    @RequestMapping("/jpa")
    public Object jap(@RequestParam("name") String name,
                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                      @RequestParam(value = "size", defaultValue = "15") Integer size) {
        org.springframework.data.domain.Page<BizJpaNews> demoPage = demoService.findByName(name, page, size);
        return ResponsePageBuilder.build(demoPage);
    }
}
