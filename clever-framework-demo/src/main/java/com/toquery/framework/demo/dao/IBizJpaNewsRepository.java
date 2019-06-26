package com.toquery.framework.demo.dao;

import com.toquery.framework.demo.entity.BizJpaNews;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author toquery
 * @version 1
 */

public interface IBizJpaNewsRepository extends AppJpaBaseRepository<BizJpaNews, Long> {

    @SuppressWarnings("MybatisMapperMethodInspection")
    @Query("from BizJpaNews where name = :name")
    public BizJpaNews getByName(@Param("name") String name);
}
