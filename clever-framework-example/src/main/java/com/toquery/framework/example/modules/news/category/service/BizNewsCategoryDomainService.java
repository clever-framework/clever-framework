package com.toquery.framework.example.modules.news.category.service;

import com.toquery.framework.example.modules.news.category.entity.BizNewsCategory;
import com.toquery.framework.example.modules.news.category.repository.BizNewsCategoryRepository;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Service
public class BizNewsCategoryDomainService extends AppBaseServiceImpl<BizNewsCategory, BizNewsCategoryRepository> {

    public static final Map<String, String> QUERY_EXPRESSIONS = new HashMap<String, String>() {
        {
            put("typeName", "typeName:EQ");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return QUERY_EXPRESSIONS;
    }
}
