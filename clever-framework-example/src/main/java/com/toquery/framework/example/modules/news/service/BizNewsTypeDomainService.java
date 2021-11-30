package com.toquery.framework.example.modules.news.service;

import com.toquery.framework.example.modules.news.entity.BizNewsType;
import com.toquery.framework.example.modules.news.repository.BizNewsTypeRepository;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Service
public class BizNewsTypeDomainService extends AppBaseServiceImpl<BizNewsType, BizNewsTypeRepository> {

    public static final Map<String, String> QUERY_EXPRESSIONS = new HashMap<String, String>() {
        {
            put("typeId", "typeId:EQ");
            put("newsId", "newsId:EQ");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return QUERY_EXPRESSIONS;
    }


}
