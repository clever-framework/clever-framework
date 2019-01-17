package com.toquery.framework.demo.service.impl;

import com.google.common.collect.Sets;
import com.toquery.framework.demo.dao.IMyBatisDemoDao;
import com.toquery.framework.demo.entity.TbMyBatisDemo;
import com.toquery.framework.demo.service.IMyBatisDemoService;
import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Service
public class MyBatisDemoServiceImpl extends AppBaseServiceImpl<Long, TbMyBatisDemo, IMyBatisDemoDao> implements IMyBatisDemoService {

    @Override
    public boolean isEnableQueryAllRecord() {
        return true;
    }

    @Override
    public Map<String, String> getQueryExpressions() {
        return null;
    }

    @Resource
    private IMyBatisDemoDao myBatisDemoDao;

    @Override
    public TbMyBatisDemo getByName(String name) {
        return myBatisDemoDao.getByMyBatisName(name);
    }

    @Override
    public TbMyBatisDemo getByName3(String name) {
        return myBatisDemoDao.getByName2(name);
    }

    @Override
    public TbMyBatisDemo update(Long id, String name) {
        TbMyBatisDemo tbMyBatisDemo = new TbMyBatisDemo(id, name);
        return myBatisDemoDao.update(tbMyBatisDemo, Sets.newHashSet("name"));
    }
}
