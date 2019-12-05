package com.toquery.framework.demo.service;


import com.toquery.framework.demo.entity.BizJpaNews;
import io.github.toquery.framework.crud.service.AppBaseService;
import org.springframework.data.domain.Page;

/**
 * @author toquery
 * @version 1
 */

public interface IBizJpaNewsService extends AppBaseService<BizJpaNews, Long> {
    BizJpaNews getByName(String name);

    BizJpaNews update(Long id, String name);

    BizJpaNews getById(Long id);

    Page<BizJpaNews> findByName(String name, Integer page, Integer size);
}
