package com.toquery.framework.example.bff.admin.news.model.mapper;

import com.toquery.framework.example.bff.admin.news.model.request.BizNewsImportRequest;
import com.toquery.framework.example.bff.admin.news.model.request.BizNewsPageRequest;
import com.toquery.framework.example.bff.admin.news.model.response.BizNewsExportResponse;
import com.toquery.framework.example.bff.admin.news.model.response.BizNewsInfoResponse;
import com.toquery.framework.example.bff.admin.news.model.response.BizNewsListResponse;
import com.toquery.framework.example.bff.admin.news.model.response.BizNewsPageResponse;
import com.toquery.framework.example.bff.admin.news.model.response.BizNewsTreeResponse;
import com.toquery.framework.example.modules.news.entity.BizNews;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 传输对象转换类
 *
 * @author toquery
 * @version 1
 */
@Mapper(componentModel = "spring")
public interface BizNewsMapper {

    BizNewsMapper INSTANCE = Mappers.getMapper(BizNewsMapper.class);

    /**
     * 请求对象转为实体对象
     *
     * @param bizNewsPageRequest 请求对象
     * @return 实体对象
     */
    BizNews page2BizNews(BizNewsPageRequest bizNewsPageRequest);

    /**
     * 请求对象转为实体对象
     *
     * @param bizNewsImportRequest 请求对象
     * @return 实体对象
     */
    BizNews import2BizNews(BizNewsImportRequest bizNewsImportRequest);

    /**
     * 实体对象转为响应page对象
     *
     * @param bizNews 实体对象
     * @return 响应对象
     */
    BizNewsPageResponse bizNews2Page(BizNews bizNews);

    /**
     * 实体对象转为响应list对象
     *
     * @param bizNews 实体对象
     * @return 响应对象
     */
    BizNewsListResponse bizNews2List(BizNews bizNews);

    /**
     * 实体对象转为响应tree对象
     *
     * @param bizNews 实体对象
     * @return 响应对象
     */
    BizNewsTreeResponse bizNews2Tree(BizNews bizNews);

    /**
     * 实体对象转为响应info对象
     *
     * @param bizNews 实体对象
     * @return 响应对象
     */
    BizNewsInfoResponse bizNews2Info(BizNews bizNews);

    /**
     * 实体对象转为响应导出对象
     *
     * @param bizNews 实体对象
     * @return 响应对象
     */
    BizNewsExportResponse bizNews2Export(BizNews bizNews);
}
