package com.toquery.framework.example.dao;

import com.toquery.framework.example.entity.BizNews;
import com.toquery.framework.example.entity.BizType;
import io.github.toquery.framework.dao.annotation.JpaParam;
import io.github.toquery.framework.dao.annotation.MybatisParam;
import io.github.toquery.framework.dao.annotation.MybatisQuery;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;
import java.util.List;

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

