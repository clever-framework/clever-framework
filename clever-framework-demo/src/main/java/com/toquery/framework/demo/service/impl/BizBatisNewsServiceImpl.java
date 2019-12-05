package com.toquery.framework.demo.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Sets;
import com.toquery.framework.demo.dao.IBizBatisNewsMapper;
import com.toquery.framework.demo.entity.BizBatisNews;
import com.toquery.framework.demo.service.IBizBatisNewsService;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Service
public class BizBatisNewsServiceImpl extends AppBaseServiceImpl<Long, BizBatisNews, IBizBatisNewsMapper> implements IBizBatisNewsService {

    @Override
    public boolean isEnableQueryAllRecord() {
        return true;
    }

    @Override
    public Map<String, String> getQueryExpressions() {
        return null;
    }

    @Resource
    private IBizBatisNewsMapper myBatisDemoDao;

    @Override
    public BizBatisNews getByName(String name) {
        return myBatisDemoDao.getByMyBatisName(name);
    }

    @Override
    public BizBatisNews getByName3(String name) {
        return myBatisDemoDao.getByName2(name);
    }

    @Override
    public BizBatisNews update(Long id, String name) {
        BizBatisNews tbMyBatisDemo = new BizBatisNews(id, name);
        return myBatisDemoDao.update(tbMyBatisDemo, Sets.newHashSet("name"));
    }

    @Override
    public Page<BizBatisNews> findByName(String name, Integer page, Integer size) {
        Page<BizBatisNews> page1 = PageHelper.startPage(page,size);
        return myBatisDemoDao.findByMyBatisName(name);
    }
}
