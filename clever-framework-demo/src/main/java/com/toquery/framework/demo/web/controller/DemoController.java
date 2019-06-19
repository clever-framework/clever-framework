package com.toquery.framework.demo.web.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import com.toquery.framework.demo.entity.TbJpaDemo;
import com.toquery.framework.demo.entity.TbMyBatisDemo;
import com.toquery.framework.demo.service.IJpaDemoService;
import com.toquery.framework.demo.service.IMyBatisDemoService;
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
public class DemoController extends AppBaseCurdController<IJpaDemoService,TbJpaDemo,Long> {

    @Resource
    private IJpaDemoService demoService;

    @Resource
    private IMyBatisDemoService myBatisDemoService;

    @RequestMapping({"/", "/index"})
    public String index() {
        return "OK";
    }

    @RequestMapping("/jpa/find")
    public Page<TbJpaDemo> findByName(@RequestParam("name") String name,
                                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "15") Integer size) {
        return demoService.findByName(name, page, size);
    }


    @RequestMapping("/jpa/get")
    public TbJpaDemo getByName1(@RequestParam("name") String name) {
        return demoService.getByName(name);
    }

    @RequestMapping("/jpa/get/{id}")
    public TbJpaDemo getById(@PathVariable("id") Long id) {
        return demoService.getById(id);
    }


    @RequestMapping("/jpa/update")
    public TbJpaDemo jpaUpdate(@RequestParam Long id, @RequestParam String name) {
        return demoService.update(id, name);
    }


    @RequestMapping("/jpa/update2")
    public TbJpaDemo jpaUpdate2(TbJpaDemo tbJpaDemo) {
        return demoService.update(tbJpaDemo, Sets.newHashSet("name"));
    }


    @RequestMapping("/mybatis/find")
    public PageInfo<TbMyBatisDemo> mybatisFindByName(@RequestParam("name") String name,
                                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "15") Integer size) {

        PageInfo<TbMyBatisDemo> pageInfo = new PageInfo<>(myBatisDemoService.findByName(name, page, size));
        return pageInfo;
    }


    @RequestMapping("/mybatis/get")
    public TbMyBatisDemo getByName2(@RequestParam String name) {
        return myBatisDemoService.getByName(name);
    }

    @RequestMapping("/mybatis/jpa/get")
    public TbMyBatisDemo getByName3(@RequestParam String name) {
        return myBatisDemoService.getByName3(name);
    }
}
