package com.toquery.framework.demo.dao;

import com.toquery.framework.demo.entity.BizJpaNewsSub;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author toquery
 * @version 1
 */

public interface IBizJpaNewsSubRepository extends AppJpaBaseRepository<BizJpaNewsSub, Long> {

    @SuppressWarnings("MybatisMapperMethodInspection")
    @Query("from BizJpaNews where name = :name")
    public BizJpaNewsSub getByName(@Param("name") String name);
}
