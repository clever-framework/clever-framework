package io.github.toquery.framework.demo.web.controller;

import io.github.toquery.framework.demo.entity.TbJpaDemo;
import io.github.toquery.framework.demo.entity.TbMyBatisDemo;
import io.github.toquery.framework.demo.service.IJpaDemoService;
import io.github.toquery.framework.demo.service.IMyBatisDemoService;
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
public class DemoController {

    @Resource
    private IJpaDemoService demoService;

    @Resource
    private IMyBatisDemoService myBatisDemoService;

    @RequestMapping({"/", "/index"})
    public String index() {
        return "OK";
    }


//    @RequestMapping("/list")
//    public List<TbJpaDemo> list() {
//        return demoService.findAll(null);
//    }


    @RequestMapping("/jpa/get")
    public TbJpaDemo getByName1(@RequestParam String name) {
        return demoService.getByName(name);
    }


    @RequestMapping("/jpa/update")
    public TbJpaDemo jpaUpdate(@RequestParam Long id, @RequestParam String name) {
        return demoService.update(id, name);
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
