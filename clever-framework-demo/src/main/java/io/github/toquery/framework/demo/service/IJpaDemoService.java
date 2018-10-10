package io.github.toquery.framework.demo.service;

import io.github.toquery.framework.demo.entity.TbJpaDemoLong;

/**
 * @author toquery
 * @version 1
 */
public interface IJpaDemoService {//} extends AppBaseDataService<TbJpaDemoLong, Long> {
    TbJpaDemoLong getByName(String name);
}
