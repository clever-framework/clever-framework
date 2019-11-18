package com.toquery.framework.demo.web.rest;

import com.google.common.collect.Sets;
import com.toquery.framework.demo.entity.BizBatisNews;
import com.toquery.framework.demo.service.IBizBatisNewsService;
import io.github.toquery.framework.curd.controller.AppBaseCurdController;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/biz-batis-news")
public class BizBatisNewsController extends AppBaseCurdController<IBizBatisNewsService, BizBatisNews, Long> {

    @GetMapping
    public ResponseParam query() {
        return super.query();
    }

    @GetMapping("/list")
    public ResponseParam list() {
        return super.list();
    }

    @PostMapping
    public ResponseParam save(@Validated @RequestBody BizBatisNews bizBatisNews) {
        return super.save(bizBatisNews);
    }

    @PutMapping
    public ResponseParam update(@RequestBody BizBatisNews bizBatisNews) {
        return super.update(bizBatisNews, Sets.newHashSet("name", "showTime"));
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
