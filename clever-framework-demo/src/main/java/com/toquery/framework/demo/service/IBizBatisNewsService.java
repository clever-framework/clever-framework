package com.toquery.framework.demo.service;


import com.github.pagehelper.Page;
import com.toquery.framework.demo.entity.BizBatisNews;
import io.github.toquery.framework.crud.service.AppBaseService;

/**
 * @author toquery
 * @version 1
 */
public interface IBizBatisNewsService extends AppBaseService<BizBatisNews, Long> {
    BizBatisNews getByName(String name);

    BizBatisNews getByName3(String name);

    BizBatisNews update(Long id, String name);

    Page<BizBatisNews> findByName(String name, Integer page, Integer size);
}
