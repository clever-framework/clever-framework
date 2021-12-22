package com.toquery.framework.example.bff.admin.news.category.model.mapper;

import com.toquery.framework.example.bff.admin.news.category.model.request.BizNewsCategoryAddRequest;
import com.toquery.framework.example.bff.admin.news.category.model.request.BizNewsCategoryImportRequest;
import com.toquery.framework.example.bff.admin.news.category.model.request.BizNewsCategoryPageRequest;
import com.toquery.framework.example.bff.admin.news.category.model.request.BizNewsCategoryUpdateRequest;
import com.toquery.framework.example.bff.admin.news.category.model.response.BizNewsCategoryExportResponse;
import com.toquery.framework.example.bff.admin.news.category.model.response.BizNewsCategoryInfoResponse;
import com.toquery.framework.example.bff.admin.news.category.model.response.BizNewsCategoryListResponse;
import com.toquery.framework.example.bff.admin.news.category.model.response.BizNewsCategoryPageResponse;
import com.toquery.framework.example.modules.news.category.entity.BizNewsCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.List;

/**
 *
 */
@Mapper(componentModel = "spring")
public interface BizNewsCategoryMapper {

    BizNewsCategoryMapper INSTANCE = Mappers.getMapper(BizNewsCategoryMapper.class);

    /**
     * 请求对象转为实体对象
     *
     * @param bizNewsCategoryPageRequest 请求对象
     * @return 实体对象
     */
    BizNewsCategory page2BizNewsCategory(BizNewsCategoryPageRequest bizNewsCategoryPageRequest);

    /**
     * 请求对象转为实体对象
     *
     * @param bizNewsCategoryPageRequest 请求对象
     * @return 实体对象
     */
    HashMap<String, Object> page2Map(BizNewsCategoryPageRequest bizNewsCategoryPageRequest);

    /**
     * 请求对象转为实体对象
     *
     * @param bizNewsCategoryImportRequest 请求对象
     * @return 实体对象
     */
    BizNewsCategory import2BizNewsCategory(BizNewsCategoryImportRequest bizNewsCategoryImportRequest);

    /**
     * 请求对象转为实体对象
     *
     * @param bizNewsCategoryAddRequest 请求对象
     * @return 实体对象
     */
    BizNewsCategory add2BizNewsCategory(BizNewsCategoryAddRequest bizNewsCategoryAddRequest);


    /**
     * 请求对象转为实体对象
     *
     * @param bizNewsCategoryUpdateRequest 请求对象
     * @return 实体对象
     */
    BizNewsCategory update2BizNewsCategory(BizNewsCategoryUpdateRequest bizNewsCategoryUpdateRequest);


    /**
     * 实体对象转为响应page对象
     *
     * @param bizNewsCategory 实体对象
     * @return 响应对象
     */
    BizNewsCategoryPageResponse bizNewsCategory2Page(BizNewsCategory bizNewsCategory);

    /**
     * 实体对象转为响应page对象
     *
     * @param bizNewsCategoryList 实体对象
     * @return 响应对象
     */
    List<BizNewsCategoryPageResponse> bizNewsCategory2Page(List<BizNewsCategory> bizNewsCategoryList);

    /**
     * 实体对象转为响应list对象
     *
     * @param bizNewsCategory 实体对象
     * @return 响应对象
     */
    BizNewsCategoryListResponse bizNewsCategory2List(BizNewsCategory bizNewsCategory);

    /**
     * 实体对象转为响应list对象
     *
     * @param bizNewsCategory 实体对象
     * @return 响应对象
     */
    List<BizNewsCategoryListResponse> bizNewsCategory2List(List<BizNewsCategory> bizNewsCategory);


    /**
     * 实体对象转为响应info对象
     *
     * @param bizNewsCategory 实体对象
     * @return 响应对象
     */
    BizNewsCategoryInfoResponse bizNewsCategory2Info(BizNewsCategory bizNewsCategory);

    /**
     * 实体对象转为响应导出对象
     *
     * @param bizNewsCategory 实体对象
     * @return 响应对象
     */
    BizNewsCategoryExportResponse bizNewsCategory2Export(BizNewsCategory bizNewsCategory);
}
