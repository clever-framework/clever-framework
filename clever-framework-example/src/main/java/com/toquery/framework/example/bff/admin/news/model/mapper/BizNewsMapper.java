package com.toquery.framework.example.bff.admin.news.model.mapper;

import com.toquery.framework.example.bff.admin.news.model.request.BizNewsPageRequest;
import com.toquery.framework.example.bff.admin.news.model.response.BizNewsPageResponse;
import com.toquery.framework.example.modules.news.entity.BizNews;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 */
@Mapper(componentModel = "spring")
public interface BizNewsMapper {

    BizNewsMapper INSTANCE = Mappers.getMapper(BizNewsMapper.class);

    BizNews request2Entity(BizNewsPageRequest bizNewsPageRequest);

    BizNewsPageResponse bizNews2Response(BizNews bizNews);
}
