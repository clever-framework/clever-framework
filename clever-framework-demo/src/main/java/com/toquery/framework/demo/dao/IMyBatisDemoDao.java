package com.toquery.framework.demo.dao;

import com.toquery.framework.demo.entity.TbMyBatisDemo;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;

/**
 * @author toquery
 * @version 1
 */
public interface IMyBatisDemoDao extends AppJpaBaseRepository<TbMyBatisDemo, Long> {
    @Mapper
    TbMyBatisDemo getByName(@Param("name") String name);

    @SuppressWarnings("MybatisMapperMethodInspection")
    @Query("from TbMyBatisDemo where name = :name")
    TbMyBatisDemo getByName2(@org.springframework.data.repository.query.Param("name") String name);

}
