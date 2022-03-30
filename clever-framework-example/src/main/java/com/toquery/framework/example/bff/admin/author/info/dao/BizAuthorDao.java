package com.toquery.framework.example.bff.admin.author.info.dao;

import com.toquery.framework.example.modules.author.info.entity.BizAuthor;
import io.github.toquery.framework.dao.annotation.JpaParam;
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
public interface BizAuthorDao extends AppJpaBaseRepository<BizAuthor> {

    /**
     * jpa 注解方式查询
     */
    @Query("from BizAuthor where authorName like :authorName")
    List<BizAuthor> findJpaByAuthorName(@JpaParam("authorName") String authorName);

    /**
     * jpa method 方式查询
     */
    List<BizAuthor> findByAuthorNameLike(String AuthorName);

    @Query("from BizAuthor where id = :id")
    BizAuthor findJpaById(@JpaParam("id") Long id);

    @Modifying
    @Query("delete from BizAuthor where id = :id")
    void deleteJpa(@JpaParam("id") Long id);

    @Modifying
    @Query("delete from BizAuthor where id in :ids")
    void deleteJpaIds(@JpaParam("ids") Collection<Long> ids);

    void deleteBizAuthorById(@JpaParam("id") Long id);

}
