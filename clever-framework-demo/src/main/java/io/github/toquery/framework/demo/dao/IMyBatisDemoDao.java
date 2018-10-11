package io.github.toquery.framework.demo.dao;

import io.github.toquery.framework.demo.entity.TbMyBatisDemo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author toquery
 * @version 1
 */
@Mapper
public interface IMyBatisDemoDao {
    @Mapper
    TbMyBatisDemo getByName(@Param("name") String name);
}
