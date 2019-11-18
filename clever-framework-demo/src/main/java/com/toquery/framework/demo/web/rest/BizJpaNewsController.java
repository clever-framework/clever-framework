package com.toquery.framework.demo.web.rest;

import com.google.common.collect.Sets;
import com.toquery.framework.demo.entity.BizJpaNews;
import com.toquery.framework.demo.service.IBizJpaNewsService;
import io.github.toquery.framework.curd.controller.AppBaseCurdController;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/biz-jpa-news")
public class BizJpaNewsController extends AppBaseCurdController<IBizJpaNewsService, BizJpaNews, Long> {


    @GetMapping
    public ResponseParam query() {
        return super.query();
    }

    @GetMapping("/list")
    public ResponseParam list() {
        return super.list();
    }

    @PostMapping
    public ResponseParam save(@Validated @RequestBody BizJpaNews bizJpaNews) {
        return super.save(bizJpaNews);
    }

    @PutMapping
    public ResponseParam update(@RequestBody BizJpaNews bizJpaNews) {
        return super.update(bizJpaNews, Sets.newHashSet("name", "showTime"));
    }

    @DeleteMapping
    public void delete(@RequestParam Set<Long> ids) {
        super.delete(ids);
    }

    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        return super.detail(id);
    }
}
