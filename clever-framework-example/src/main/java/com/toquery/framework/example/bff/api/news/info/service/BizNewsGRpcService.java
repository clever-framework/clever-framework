package com.toquery.framework.example.bff.api.news.info.service;

import com.toquery.framework.example.bff.admin.news.category.model.response.BizNewsCategoryPageResponse;
import com.toquery.framework.example.bff.api.news.info.model.mapper.BizNewsMapper;
import com.toquery.framework.example.bff.api.news.info.model.request.BizNewsPageRequest;
import com.toquery.framework.example.bff.api.news.info.model.response.BizNewsPageResponse;
import com.toquery.framework.example.modules.news.info.entity.BizNews;
import com.toquery.framework.example.modules.news.info.service.BizNewsDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class BizNewsGRpcService {

    @Autowired
    private BizNewsMapper bizNewsMapper;
    @Autowired
    private BizNewsDomainService bizNewsDomainService;

    public Page<BizNewsPageResponse> page(BizNewsPageRequest pageRequest) {
        Page<BizNews> bizNewsPage = bizNewsDomainService.page(pageRequest.getCurrent(), pageRequest.getPageSize());
        List<BizNewsPageResponse> pageResponses = bizNewsMapper.bizNews2Page(bizNewsPage.toList());
        return new PageImpl<>(pageResponses, bizNewsPage.getPageable(), bizNewsPage.getTotalElements());
    }
}
