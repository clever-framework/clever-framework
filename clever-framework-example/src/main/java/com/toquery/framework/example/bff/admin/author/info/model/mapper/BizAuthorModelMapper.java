package com.toquery.framework.example.bff.admin.author.info.model.mapper;

import com.toquery.framework.example.bff.admin.author.info.model.request.BizAuthorAddRequest;
import com.toquery.framework.example.bff.admin.author.info.model.request.BizAuthorImportRequest;
import com.toquery.framework.example.bff.admin.author.info.model.request.BizAuthorPageRequest;
import com.toquery.framework.example.bff.admin.author.info.model.request.BizAuthorUpdateRequest;
import com.toquery.framework.example.bff.admin.author.info.model.response.BizAuthorExportResponse;
import com.toquery.framework.example.bff.admin.author.info.model.response.BizAuthorInfoResponse;
import com.toquery.framework.example.bff.admin.author.info.model.response.BizAuthorListResponse;
import com.toquery.framework.example.bff.admin.author.info.model.response.BizAuthorPageResponse;
import com.toquery.framework.example.modules.author.info.entity.BizAuthor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.List;

/**
 * 传输对象转换类
 *
 * @author toquery
 * @version 1
 */
@Mapper(componentModel = "spring")
public interface BizAuthorModelMapper {

    BizAuthorModelMapper INSTANCE = Mappers.getMapper(BizAuthorModelMapper.class);

    /**
     * 请求对象转为实体对象
     *
     * @param bizAuthorPageRequest 请求对象
     * @return 实体对象
     */
    BizAuthor page2BizAuthor(BizAuthorPageRequest bizAuthorPageRequest);

    /**
     * 请求对象转为实体对象
     *
     * @param bizAuthorPageRequest 请求对象
     * @return 实体对象
     */
    HashMap<String, Object> page2Map(BizAuthorPageRequest bizAuthorPageRequest);

    /**
     * 请求对象转为实体对象
     *
     * @param bizAuthorImportRequest 请求对象
     * @return 实体对象
     */
    BizAuthor import2BizAuthor(BizAuthorImportRequest bizAuthorImportRequest);

    /**
     * 请求对象转为实体对象
     *
     * @param bizAuthorAddRequest 请求对象
     * @return 实体对象
     */
    BizAuthor add2BizAuthor(BizAuthorAddRequest bizAuthorAddRequest);



    /**
     * 请求对象转为实体对象
     *
     * @param bizAuthorUpdateRequest 请求对象
     * @return 实体对象
     */
    BizAuthor update2BizAuthor(BizAuthorUpdateRequest bizAuthorUpdateRequest);


    /**
     * 实体对象转为响应page对象
     *
     * @param bizAuthor 实体对象
     * @return 响应对象
     */
    BizAuthorPageResponse bizAuthor2Page(BizAuthor bizAuthor);

    /**
     * 实体对象转为响应list对象
     *
     * @param bizAuthor 实体对象
     * @return 响应对象
     */
    BizAuthorListResponse bizAuthor2List(BizAuthor bizAuthor);

    /**
     * 实体对象转为响应list对象
     *
     * @param bizAuthor 实体对象
     * @return 响应对象
     */
    List<BizAuthorListResponse> bizAuthor2List(List<BizAuthor> bizAuthor);


    /**
     * 实体对象转为响应info对象
     *
     * @param bizAuthor 实体对象
     * @return 响应对象
     */
    BizAuthorInfoResponse bizAuthor2Info(BizAuthor bizAuthor);

    /**
     * 实体对象转为响应导出对象
     *
     * @param bizAuthor 实体对象
     * @return 响应对象
     */
    BizAuthorExportResponse bizAuthor2Export(BizAuthor bizAuthor);
}
