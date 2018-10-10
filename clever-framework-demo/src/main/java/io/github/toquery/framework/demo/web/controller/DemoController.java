package io.github.toquery.framework.demo.web.controller;

import io.github.toquery.framework.demo.entity.TbJpaDemoLong;
import io.github.toquery.framework.demo.service.IJpaDemoService;
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

    @RequestMapping({"/", "/index"})
    public String index() {
        return "OK";
    }


//    @RequestMapping("/list")
//    public List<TbJpaDemoLong> list() {
//        return demoService.findAll(null);
//    }


    @RequestMapping("/get")
    public TbJpaDemoLong getByName(@RequestParam String getByName) {
        return demoService.getByName(getByName);
    }


}
