package io.github.toquery.framework.demo.service;

import io.github.toquery.framework.demo.entity.TbJpaDemo;

/**
 * @author toquery
 * @version 1
 */
public interface IJpaDemoService {//} extends AppBaseDataService<TbJpaDemo, Long> {
    TbJpaDemo getByName(String name);
}
