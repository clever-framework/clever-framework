package com.toquery.framework.demo.dao;

import com.github.pagehelper.Page;
import com.toquery.framework.demo.entity.TbMyBatisDemo;
import io.github.toquery.framework.dao.annotation.JpaParam;
import io.github.toquery.framework.dao.annotation.MybatisParam;
import io.github.toquery.framework.dao.jpa.annotation.MybatisQuery;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.Query;

/**
 * @author toquery
 * @version 1
 */
public interface IMyBatisDemoDao extends AppJpaBaseRepository<TbMyBatisDemo, Long> {
    @Mapper
    @MybatisQuery
    TbMyBatisDemo getByMyBatisName(@MybatisParam("name") String name);

    @Mapper
    @MybatisQuery
    Page<TbMyBatisDemo> findByMyBatisName(@MybatisParam("name") String name);

    @SuppressWarnings("MybatisMapperMethodInspection")
    @Query("from TbMyBatisDemo where name = :name")
    TbMyBatisDemo getByName2(@JpaParam("name") String name);

}
