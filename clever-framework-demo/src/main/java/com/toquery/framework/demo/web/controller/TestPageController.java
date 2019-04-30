package com.toquery.framework.demo.web.controller;

import com.toquery.framework.demo.entity.TbJpaDemo;
import com.toquery.framework.demo.entity.TbMyBatisDemo;
import com.toquery.framework.demo.service.IJpaDemoService;
import com.toquery.framework.demo.service.IMyBatisDemoService;
import io.github.toquery.framework.web.domain.ResponsePage;
import io.github.toquery.framework.web.domain.ResponsePageBuilder;
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
    private IMyBatisDemoService myBatisDemoService;

    @RequestMapping("/page")
    public ResponsePage test() {
        return new ResponsePage(12, 13, 14L, 2);
    }

    @RequestMapping("/mybatis")
    public Object mybatis(@RequestParam("name") String name,
                          @RequestParam(value = "page", defaultValue = "0") Integer page,
                          @RequestParam(value = "size", defaultValue = "15") Integer size) {
        com.github.pagehelper.Page<TbMyBatisDemo> pageInfo = myBatisDemoService.findByName(name, page, size);
        return ResponsePageBuilder.build(pageInfo);
    }

    @Resource
    private IJpaDemoService demoService;

    @RequestMapping("/jpa")
    public Object jap(@RequestParam("name") String name,
                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                      @RequestParam(value = "size", defaultValue = "15") Integer size) {
        org.springframework.data.domain.Page<TbJpaDemo> demoPage = demoService.findByName(name, page, size);
        return ResponsePageBuilder.build(demoPage);
    }
}
