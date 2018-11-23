package com.toquery.framework.demo.dao.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author toquery
 * @version 1
 */
@Mapper
public interface NewsMapper {
    int update(@Param("id") Long id, @Param("title") String title);
}
