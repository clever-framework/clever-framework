package com.toquery.framework.example.modules.news.info.service;

import com.toquery.framework.example.modules.news.info.entity.BizNews;
import com.toquery.framework.example.modules.news.info.repository.BizNewsRepository;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Service
public class BizNewsDomainService extends AppBaseServiceImpl<BizNews, BizNewsRepository> {


    public static final Map<String, String> QUERY_EXPRESSIONS = new HashMap<String, String>() {
        {
            put("num", "num:EQ");
            put("title", "title:EQ");
            put("showTime", "showTime:EQ");
            put("showStatus", "showStatus:EQ");
            put("localDateTime", "localDateTime:EQ");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return QUERY_EXPRESSIONS;
    }

}
