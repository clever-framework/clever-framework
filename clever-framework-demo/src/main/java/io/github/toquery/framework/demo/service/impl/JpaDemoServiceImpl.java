package io.github.toquery.framework.demo.service.impl;

import com.google.common.collect.Sets;
import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.demo.dao.IJpaDemoRepository;
import io.github.toquery.framework.demo.entity.TbJpaDemo;
import io.github.toquery.framework.demo.service.IJpaDemoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Service

//public class JpaDemoServiceImpceImpl implements IJpaDemoService {
public class JpaDemoServiceImpl extends AppBaseServiceImpl<Long, TbJpaDemo, IJpaDemoRepository> implements IJpaDemoService {


    @Override
    public boolean isEnableQueryAllRecord() {
        return true;
    }

    @Override
    public Map<String, String> getQueryExpressions() {
        return null;
    }


    @Resource
    private IJpaDemoRepository jpaDemoDao;

    @Override
    public TbJpaDemo getByName(String name) {
        return jpaDemoDao.getByName(name);
    }

    @Override
    public TbJpaDemo update(Long id, String name) {
        TbJpaDemo tbJpaDemo = new TbJpaDemo();
        tbJpaDemo.setId(id);
        tbJpaDemo.setName(name);
        return jpaDemoDao.update(tbJpaDemo, Sets.newHashSet("name"));
    }

    @Override
    public TbJpaDemo getById(Long id) {
        return jpaDemoDao.getOne(id);
    }

}
