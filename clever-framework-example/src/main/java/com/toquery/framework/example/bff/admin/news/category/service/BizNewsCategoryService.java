package com.toquery.framework.example.bff.admin.news.category.service;

import com.toquery.framework.example.bff.admin.news.category.dao.BizNewsCategoryDao;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsPageRequest;
import com.toquery.framework.example.modules.news.category.service.BizNewsCategoryDomainService;
import com.toquery.framework.example.modules.news.info.service.BizNewsDomainService;
import io.github.toquery.framework.crud.service.impl.AppBFFServiceImpl;
import io.github.toquery.framework.webmvc.domain.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Service
public class BizNewsCategoryService extends AppBFFServiceImpl{

    @Autowired
    private BizNewsCategoryDomainService bizNewsCategoryDomainService;
    @Autowired
    private BizNewsCategoryDao bizNewsCategoryDao;


    public static final Map<String, String> QUERY_EXPRESSIONS = new HashMap<String, String>(BizNewsDomainService.QUERY_EXPRESSIONS) {
        {
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return QUERY_EXPRESSIONS;
    }

    public ResponseBody page(BizNewsPageRequest bizNewsPageRequest) {
       // bizNewsCategoryDomainService.queryByPage(),
        return null;
    }
}
