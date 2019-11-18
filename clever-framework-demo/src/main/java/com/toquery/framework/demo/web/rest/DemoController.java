package com.toquery.framework.demo.web.rest;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import com.toquery.framework.demo.entity.BizJpaNews;
import com.toquery.framework.demo.entity.BizBatisNews;
import com.toquery.framework.demo.service.IBizJpaNewsService;
import com.toquery.framework.demo.service.IBizBatisNewsService;
import io.github.toquery.framework.curd.controller.AppBaseCurdController;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/demo")
public class DemoController extends AppBaseCurdController<IBizJpaNewsService, BizJpaNews,Long> {

    @Resource
    private IBizJpaNewsService demoService;

    @Resource
    private IBizBatisNewsService myBatisDemoService;

    @RequestMapping({"/", "/index"})
    public String index() {
        return "OK";
    }

    @RequestMapping("/jpa/find")
    public Page<BizJpaNews> findByName(@RequestParam("name") String name,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                       @RequestParam(value = "size", defaultValue = "15") Integer size) {
        return demoService.findByName(name, page, size);
    }


    @RequestMapping("/jpa/get")
    public BizJpaNews getByName1(@RequestParam("name") String name) {
        return demoService.getByName(name);
    }

    @RequestMapping("/jpa/get/{id}")
    public BizJpaNews getById(@PathVariable("id") Long id) {
        return demoService.getById(id);
    }


    @RequestMapping("/jpa/update")
    public BizJpaNews jpaUpdate(@RequestParam Long id, @RequestParam String name) {
        return demoService.update(id, name);
    }


    @RequestMapping("/jpa/update2")
    public BizJpaNews jpaUpdate2(BizJpaNews tbJpaDemo) {
        return demoService.update(tbJpaDemo, Sets.newHashSet("name"));
    }


    @RequestMapping("/mybatis/find")
    public PageInfo<BizBatisNews> mybatisFindByName(@RequestParam("name") String name,
                                                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "15") Integer size) {

        PageInfo<BizBatisNews> pageInfo = new PageInfo<>(myBatisDemoService.findByName(name, page, size));
        return pageInfo;
    }


    @RequestMapping("/mybatis/get")
    public BizBatisNews getByName2(@RequestParam String name) {
        return myBatisDemoService.getByName(name);
    }

    @RequestMapping("/mybatis/jpa/get")
    public BizBatisNews getByName3(@RequestParam String name) {
        return myBatisDemoService.getByName3(name);
    }
}
