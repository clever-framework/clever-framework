package com.toquery.framework.example.bff.admin.news.info.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toquery.framework.example.modules.news.info.entity.BizNews;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public interface BizNewsMapper extends BaseMapper<BizNews> {
    /**
     * mybatis 方式查询
     */
    List<BizNews> listMyBatis();

    void saveMyBatis(BizNews bizNews);

    BizNews getMyBatisById(Long id);

    BizNews updateMyBatis(BizNews bizNews);

    void deleteMyBatis(Collection<Long> ids);

}
