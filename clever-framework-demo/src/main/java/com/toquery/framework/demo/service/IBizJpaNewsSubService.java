package com.toquery.framework.demo.service;


import com.toquery.framework.demo.entity.BizJpaNewsSub;
import io.github.toquery.framework.crud.service.AppBaseService;
import org.springframework.data.domain.Page;

/**
 * @author toquery
 * @version 1
 */

public interface IBizJpaNewsSubService extends AppBaseService<BizJpaNewsSub, Long> {
    BizJpaNewsSub getByName(String name);

    BizJpaNewsSub update(Long id, String name);

    BizJpaNewsSub getById(Long id);

    Page<BizJpaNewsSub> findByName(String name, Integer page, Integer size);
}
