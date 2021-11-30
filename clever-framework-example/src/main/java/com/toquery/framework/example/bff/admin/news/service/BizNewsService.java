package com.toquery.framework.example.bff.admin.news.service;

import com.toquery.framework.example.bff.admin.news.mapper.BizNewsDao;
import com.toquery.framework.example.modules.news.entity.BizNews;
import com.toquery.framework.example.modules.news.repository.BizNewsRepository;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.toquery.framework.example.modules.news.service.BizNewsDomainService.QUERY_EXPRESSIONS;

/**
 *
 */
@Service
public class BizNewsService extends AppBaseServiceImpl<BizNews, BizNewsRepository> {

    @Autowired
    private BizNewsDao bizNewsDao;

    @Override
    public Map<String, String> getQueryExpressions() {
        QUERY_EXPRESSIONS.put("titleLIKE", "title:LIKE");
        QUERY_EXPRESSIONS.put("showTimeGT", "showTime:GT");
        QUERY_EXPRESSIONS.put("showTimeLT", "showTime:LT");
        QUERY_EXPRESSIONS.put("showTimeGE", "showTime:GE");
        QUERY_EXPRESSIONS.put("showTimeLE", "showTime:LE");

        QUERY_EXPRESSIONS.put("localDateTimeGT", "localDateTime:GT");
        QUERY_EXPRESSIONS.put("localDateTimeLT", "localDateTime:LT");
        QUERY_EXPRESSIONS.put("localDateTimeGE", "localDateTime:GE");
        QUERY_EXPRESSIONS.put("localDateTimeLE", "localDateTime:LE");
        return QUERY_EXPRESSIONS;
    }
}
