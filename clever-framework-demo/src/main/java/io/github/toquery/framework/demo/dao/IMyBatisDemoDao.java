package io.github.toquery.framework.demo.dao;

import io.github.toquery.framework.demo.entity.TbMyBatisDemo;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;

/**
 * @author toquery
 * @version 1
 */
//@Mapper
public interface IMyBatisDemoDao extends AppJpaBaseRepository<TbMyBatisDemo, Long> {
    @Mapper
    TbMyBatisDemo getByName(@Param("name") String name);

    @Query("from TbMyBatisDemo where name = :name")
    TbMyBatisDemo getByName2(@org.springframework.data.repository.query.Param("name") String name);

}
