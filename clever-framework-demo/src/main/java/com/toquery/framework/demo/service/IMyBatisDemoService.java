package com.toquery.framework.demo.service;


import com.github.pagehelper.Page;
import com.toquery.framework.demo.entity.TbMyBatisDemo;
import io.github.toquery.framework.curd.service.AppBaseService;

/**
 * @author toquery
 * @version 1
 */
public interface IMyBatisDemoService extends AppBaseService<TbMyBatisDemo, Long> {
    TbMyBatisDemo getByName(String name);

    TbMyBatisDemo getByName3(String name);

    TbMyBatisDemo update(Long id, String name);

    Page<TbMyBatisDemo> findByName(String name, Integer page, Integer size);
}
