package com.toquery.framework.example.bff.admin.news.info.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toquery.framework.example.modules.news.info.entity.BizNews;
import io.github.toquery.framework.dao.annotation.JpaParam;
import io.github.toquery.framework.dao.annotation.MybatisParam;
import io.github.toquery.framework.dao.annotation.MybatisQuery;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 *
 */
@SuppressWarnings("MybatisMapperMethodInspection")
//@RestResource(path = "rest-resource")
//@RepositoryRestResource(path = "example-biz-news-spring")
public interface BizNewsDao extends AppJpaBaseRepository<BizNews> {

    /**
     * jpa 注解方式查询
     */
    @Query("from BizNews where title like :title")
    List<BizNews> findJpaByTitle(@JpaParam("title") String title);

    /**
     * jpa method 方式查询
     */
    List<BizNews> findByTitleLike(String title);

    /**
     * mybatis 方式查询
     */
    @Mapper
    @MybatisQuery
    List<BizNews> findMyBatisByTitle(@MybatisParam("title") String title);

    @Mapper
    @MybatisQuery
    void saveMyBatis(BizNews bizNews);

    @Query("from BizNews where id = :id")
    BizNews findJpaById(@JpaParam("id") Long id);

    @Mapper
    @MybatisQuery
    BizNews getMyBatisById(@MybatisParam("id") Long id);

    @Mapper
    @MybatisQuery
    BizNews updateMyBatis(BizNews bizNews);

    @Modifying
    @Query("delete from BizNews where id = :id")
    void deleteJpa(@JpaParam("id") Long id);

    @Modifying
    @Query("delete from BizNews where id in :ids")
    void deleteJpaIds(@JpaParam("ids") Collection<Long> ids);

    void deleteBizNewsById(@JpaParam("id") Long id);

    @Mapper
    @MybatisQuery
    void deleteMyBatis(@MybatisParam("ids") Collection<Long> ids);
}
