package com.toquery.framework.example.modules.author.info.service;

import com.toquery.framework.example.modules.author.info.entity.BizAuthor;
import com.toquery.framework.example.modules.author.info.repository.BizAuthorRepository;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
//import io.github.toquery.framework.datasource.DataSource;
import io.github.toquery.framework.datasource.DataSourceSwitch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@DataSourceSwitch("test")
@Service
//@Transactional("testTransactionManager")
public class BizAuthorDomainService extends AppBaseServiceImpl<BizAuthor, BizAuthorRepository> {


    public static final Map<String, String> QUERY_EXPRESSIONS = new HashMap<String, String>() {
        {
            put("authorName", "authorName:EQ");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return QUERY_EXPRESSIONS;
    }

}
