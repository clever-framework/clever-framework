package com.toquery.framework.demo.service;


import com.toquery.framework.demo.entity.TbJpaDemo;
import io.github.toquery.framework.curd.service.AppBaseService;
import org.springframework.data.domain.Page;

/**
 * @author toquery
 * @version 1
 */

public interface IJpaDemoService extends AppBaseService<TbJpaDemo, Long> {
    TbJpaDemo getByName(String name);

    TbJpaDemo update(Long id, String name);

    TbJpaDemo getById(Long id);

    Page<TbJpaDemo> findByName(String name, Integer page, Integer size);
}
