package io.github.toquery.framework.system.service.impl;

import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysPost;
import io.github.toquery.framework.system.repository.SysPostRepository;
import io.github.toquery.framework.system.service.ISysPostService;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
public class SysPostServiceImpl extends AppBaseServiceImpl<SysPost, SysPostRepository> implements ISysPostService {


    /**
     * 查询条件表达式
     */
    public static final Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("idIN", "id:IN");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }

}
