package io.github.toquery.framework.demo.service;

import io.github.toquery.framework.demo.entity.TbMyBatisDemo;

/**
 * @author toquery
 * @version 1
 */
public interface IMyBatisDemoService {//} extends AppBaseService<TbJpaDemo, Long> {
    TbMyBatisDemo getByName(String name);

    TbMyBatisDemo getByName3(String name);
}
