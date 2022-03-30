package com.toquery.framework.example.bff.admin.author.info.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toquery.framework.example.modules.author.info.entity.BizAuthor;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public interface BizAuthorMapper extends BaseMapper<BizAuthor> {

    List<BizAuthor> listMyBatis();

    void saveMyBatis(BizAuthor BizAuthor);


    BizAuthor findMyBatisById( Long id);

    BizAuthor updateMyBatis(BizAuthor BizAuthor);

    void deleteMyBatis( Collection<Long> ids);
}
