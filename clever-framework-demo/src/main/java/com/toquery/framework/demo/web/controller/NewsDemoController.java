package com.toquery.framework.demo.web.controller;

import com.toquery.framework.demo.entity.TbNewsDemo;
import com.toquery.framework.demo.service.INewsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/news")
public class NewsDemoController {

    @Resource
    private INewsService newsService;
    @RequestMapping("/update")
    public void update(TbNewsDemo news){
        newsService.update(news);
    }
}
