package io.github.toquery.framework.demo.service.impl;

import io.github.toquery.framework.demo.dao.IMyBatisDemoDao;
import io.github.toquery.framework.demo.entity.TbMyBatisDemo;
import io.github.toquery.framework.demo.service.IMyBatisDemoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@Service
public class MyBatisDemoServiceImpl implements IMyBatisDemoService { //extends AppJPABaseDataServiceImpl<TbJpaDemo, IJpaDemoDao> implements IJpaDemoService {

   /* @Override
    public boolean isEnableQueryAllRecord() {
        return true;
    }

    @Override
    public Map<String, String> getQueryExpressions() {
        return null;
    }*/

   @Resource
   private IMyBatisDemoDao myBatisDemoDao;

    @Override
    public TbMyBatisDemo getByName(String name) {
        return myBatisDemoDao.getByName(name);
    }
}
