package com.toquery.framework.example.bff.admin.news.category.dao;

import com.toquery.framework.example.modules.news.category.entity.BizNewsCategory;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 *
 */
@SuppressWarnings("MybatisMapperMethodInspection")
@RestResource(path = "rest-resource")
@RepositoryRestResource(path = "example-biz-news-category-spring")
public interface BizNewsCategoryDao extends AppJpaBaseRepository<BizNewsCategory> {

}
