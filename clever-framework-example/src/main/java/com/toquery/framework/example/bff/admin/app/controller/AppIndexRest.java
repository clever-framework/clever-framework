package com.toquery.framework.example.bff.admin.app.controller;

import com.toquery.framework.example.modules.news.info.entity.BizNews;
import com.toquery.framework.example.modules.news.info.service.BizNewsDomainService;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/admin")
public class AppIndexRest extends AppBaseCrudController<BizNewsDomainService, BizNews> {


    @GetMapping({"", "/", "/index"})
    public String index() {
        return "Hello World!";
    }

}
