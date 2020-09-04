package com.toquery.framework.example.rest;

import com.google.common.collect.Sets;
import com.toquery.framework.example.constant.QueryType;
import com.toquery.framework.example.entity.BizNews;
import com.toquery.framework.example.service.IBizNewsService;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.webmvc.annotation.UpperCase;
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

import java.util.List;
import java.util.Set;

/**
 * 常量
 *
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/biz-news")
public class BizNewsRest extends AppBaseCrudController<IBizNewsService, BizNews, Long> {


    @GetMapping
    public ResponseParam query(@RequestParam(name = "queryType", defaultValue = "APP") @UpperCase QueryType queryType) {
        ResponseParam responseParam = null;
        switch (queryType) {
            case APP: {
                responseParam = super.query();
                break;
            }
            case JPA: {
                org.springframework.data.domain.Page<BizNews> bizNewsPage = super.service.queryJpaByPage(super.getRequestPageNumber(), super.getRequestPageSize());
                responseParam = ResponseParam.builder().page(bizNewsPage).build();
                break;
            }
            case MYBATIS: {
                com.github.pagehelper.Page<BizNews> bizNewsPage = super.service.queryMyBatisByPage(super.getRequestPageNumber(), super.getRequestPageSize());
                responseParam = ResponseParam.builder().page(bizNewsPage).build();
                break;
            }
        }
        return responseParam;
    }

    @GetMapping("/list")
    public ResponseParam list(@RequestParam(defaultValue = "APP") @UpperCase QueryType queryType) {
        ResponseParam responseParam = null;
        switch (queryType) {
            case APP: {
                responseParam = super.list();
                break;
            }
            case JPA: {
                List<BizNews> bizNews = super.service.listJpa();
                responseParam = ResponseParam.builder().content(bizNews).build();
                break;
            }
            case MYBATIS: {
                List<BizNews> bizNews = super.service.listMyBatis();
                responseParam = ResponseParam.builder().content(bizNews).build();
                break;
            }
        }
        return responseParam;
    }

    @PostMapping
    public ResponseParam save(@RequestParam(defaultValue = "APP") @UpperCase QueryType queryType, @Validated @RequestBody BizNews bizNews) {
        ResponseParam responseParam = null;
        switch (queryType) {
            case APP: {
                responseParam = super.save(bizNews);
                break;
            }
            case JPA: {
                bizNews = super.service.saveJpa(bizNews);
                responseParam = ResponseParam.builder().content(bizNews).build();
                break;
            }
            case MYBATIS: {
                bizNews = super.service.saveMyBatis(bizNews);
                responseParam = ResponseParam.builder().content(bizNews).build();
                break;
            }
        }
        return responseParam;
    }

    @PutMapping
    public ResponseParam update(@UpperCase @RequestParam(defaultValue = "APP") QueryType queryType, @RequestBody BizNews bizNews) {
        ResponseParam responseParam = null;
        switch (queryType) {
            case APP: {
                responseParam = super.update(bizNews, Sets.newHashSet("name", "showTime"));
                break;
            }
            case JPA: {
                bizNews = super.service.updateJpa(bizNews);
                responseParam = ResponseParam.builder().content(bizNews).build();
                break;
            }
            case MYBATIS: {
                bizNews = super.service.updateMyBatis(bizNews);
                responseParam = ResponseParam.builder().content(bizNews).build();
                break;
            }
        }
        return responseParam;
    }

    @DeleteMapping
    public void delete(@UpperCase @RequestParam(defaultValue = "APP") QueryType queryType, @RequestParam Set<Long> ids) {
        switch (queryType) {
            case APP: {
                super.delete(ids);
                break;
            }
            case JPA: {
                super.service.deleteJpa(ids);
                break;
            }
            case MYBATIS: {
                super.service.deleteMyBatis(ids);
                break;
            }
        }

    }

    @GetMapping("{id}")
    public ResponseParam detail(@RequestParam(defaultValue = "APP") @UpperCase QueryType queryType, @PathVariable Long id) {
        ResponseParam responseParam = null;
        switch (queryType) {
            case APP: {
                responseParam = super.detail(id);
                break;
            }
            case JPA: {
                BizNews bizNews = super.service.detailJpa(id);
                responseParam = ResponseParam.builder().content(bizNews).build();
                break;
            }
            case MYBATIS: {
                BizNews bizNews = super.service.detailMyBatis(id);
                responseParam = ResponseParam.builder().content(bizNews).build();
                break;
            }
        }
        return responseParam;
    }
}
