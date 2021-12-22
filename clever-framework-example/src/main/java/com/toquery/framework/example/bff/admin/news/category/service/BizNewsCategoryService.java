package com.toquery.framework.example.bff.admin.news.category.service;

import com.toquery.framework.example.bff.admin.news.category.dao.BizNewsCategoryDao;
import com.toquery.framework.example.bff.admin.news.category.model.mapper.BizNewsCategoryMapper;
import com.toquery.framework.example.bff.admin.news.category.model.request.BizNewsCategoryAddRequest;
import com.toquery.framework.example.bff.admin.news.category.model.request.BizNewsCategoryUpdateRequest;
import com.toquery.framework.example.bff.admin.news.category.model.response.BizNewsCategoryInfoResponse;
import com.toquery.framework.example.bff.admin.news.category.model.response.BizNewsCategoryListResponse;
import com.toquery.framework.example.bff.admin.news.category.model.response.BizNewsCategoryPageResponse;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsListRequest;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsPageRequest;
import com.toquery.framework.example.modules.news.category.entity.BizNewsCategory;
import com.toquery.framework.example.modules.news.category.service.BizNewsCategoryDomainService;
import com.toquery.framework.example.modules.news.info.service.BizNewsDomainService;
import io.github.toquery.framework.crud.service.impl.AppBFFServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@Service
@AllArgsConstructor
public class BizNewsCategoryService extends AppBFFServiceImpl {

    private BizNewsCategoryDao bizNewsCategoryDao;

    private BizNewsCategoryMapper bizNewsCategoryMapper;

    private BizNewsCategoryDomainService bizNewsCategoryDomainService;

    public static final Map<String, String> QUERY_EXPRESSIONS = new HashMap<String, String>(BizNewsDomainService.QUERY_EXPRESSIONS) {
        {
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return QUERY_EXPRESSIONS;
    }

    public Page<BizNewsCategoryPageResponse> page(BizNewsPageRequest pageRequest) {
        Page<BizNewsCategory> bizNewsCategoryPage = bizNewsCategoryDomainService.page(pageRequest.getCurrent(), pageRequest.getPageSize());
        List<BizNewsCategoryPageResponse> pageResponses = bizNewsCategoryMapper.bizNewsCategory2Pages(bizNewsCategoryPage.toList());
        return new PageImpl<>(pageResponses, bizNewsCategoryPage.getPageable(), bizNewsCategoryPage.getTotalElements());
    }

    public List<BizNewsCategoryListResponse> list(BizNewsListRequest listRequest) {
        List<BizNewsCategory> bizNewsCategoryList = bizNewsCategoryDomainService.list();
        return bizNewsCategoryMapper.bizNewsCategory2List(bizNewsCategoryList);
    }

    public BizNewsCategoryInfoResponse save(BizNewsCategoryAddRequest addRequest) {
        BizNewsCategory bizNewsCategory = bizNewsCategoryMapper.add2BizNewsCategory(addRequest);
        bizNewsCategory = bizNewsCategoryDomainService.save(bizNewsCategory);
        return bizNewsCategoryMapper.bizNewsCategory2Info(bizNewsCategory);
    }

    public BizNewsCategoryInfoResponse update(BizNewsCategoryUpdateRequest updateRequest) {
        BizNewsCategory bizNewsCategory = bizNewsCategoryMapper.update2BizNewsCategory(updateRequest);
        bizNewsCategory = bizNewsCategoryDomainService.update(bizNewsCategory);
        return bizNewsCategoryMapper.bizNewsCategory2Info(bizNewsCategory);
    }

    public void delete(Set<Long> ids) {
        bizNewsCategoryDomainService.deleteByIds(ids);
    }

    public BizNewsCategoryInfoResponse get(Long id) {
        BizNewsCategory bizNewsCategory = bizNewsCategoryDomainService.getById(id);
        return bizNewsCategoryMapper.bizNewsCategory2Info(bizNewsCategory);
    }
}
