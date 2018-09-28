package io.github.toquery.framework.demo.service.impl;

import io.github.toquery.framework.demo.dao.IJpaDemoDao;
import io.github.toquery.framework.demo.entity.TbJpaDemo;
import io.github.toquery.framework.demo.service.IJpaDemoService;
import io.github.toquery.framework.support.service.jpa.AppJPABaseDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Service
public class JpaDemoServiceImpl implements IJpaDemoService{ //extends AppJPABaseDataServiceImpl<TbJpaDemo, IJpaDemoDao> implements IJpaDemoService {

   /* @Override
    public boolean isEnableQueryAllRecord() {
        return true;
    }

    @Override
    public Map<String, String> getQueryExpressions() {
        return null;
    }*/

   @Autowired
   private IJpaDemoDao jpaDemoDao;

    @Override
    public TbJpaDemo getByName(String name) {
        return jpaDemoDao.getByName(name);
    }
}
