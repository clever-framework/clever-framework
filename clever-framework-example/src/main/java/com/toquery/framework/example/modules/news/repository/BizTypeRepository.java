package com.toquery.framework.example.modules.news.repository;

import com.toquery.framework.example.modules.news.entity.BizType;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author toquery
 * @version 1
 */
@RepositoryRestResource(path = "example-biz-type")
public interface BizTypeRepository extends AppJpaBaseRepository<BizType> {

}

