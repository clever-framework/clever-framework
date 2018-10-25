package io.github.toquery.framework.demo.service;

import io.github.toquery.framework.curd.service.AppBaseService;
import io.github.toquery.framework.demo.entity.TbMyBatisDemo;

/**
 * @author toquery
 * @version 1
 */
public interface IMyBatisDemoService  extends AppBaseService<TbMyBatisDemo, Long> {
    TbMyBatisDemo getByName(String name);

    TbMyBatisDemo getByName3(String name);

    TbMyBatisDemo update(Long id, String name);
}
