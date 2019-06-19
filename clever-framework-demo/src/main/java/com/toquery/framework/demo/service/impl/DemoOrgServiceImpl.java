package com.toquery.framework.demo.service.impl;

import com.toquery.framework.demo.dao.IDemoUserDao;
import com.toquery.framework.demo.entity.DemoUser;
import com.toquery.framework.demo.service.IDemoUserService;
import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Service
public class DemoOrgServiceImpl extends AppBaseServiceImpl<Long, DemoUser, IDemoUserDao> implements IDemoUserService {


    private static final Map<String, String> map = new HashMap<String, String>() {
        {
            put("name", "name:EQ");
        }
    };


    @Override
    public Map<String, String> getQueryExpressions() {
        return map;
    }
}
