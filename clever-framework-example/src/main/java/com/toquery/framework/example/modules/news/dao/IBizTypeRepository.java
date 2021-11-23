package com.toquery.framework.example.modules.news.dao;

import com.toquery.framework.example.modules.news.entity.BizType;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * 通过
 *
 * @author toquery
 * @version 1
 */
@SuppressWarnings("MybatisMapperMethodInspection")
@RepositoryRestResource(path = "example-biz-type")
public interface IBizTypeRepository extends AppJpaBaseRepository<BizType> {

}

