package com.toquery.framework.demo.service.impl;

import com.google.common.collect.Sets;
import com.toquery.framework.demo.dao.IJpaDemoRepository;
import com.toquery.framework.demo.entity.TbJpaDemo;
import com.toquery.framework.demo.service.IJpaDemoService;
import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Service
public class JpaDemoServiceImpl extends AppBaseServiceImpl<Long, TbJpaDemo, IJpaDemoRepository> implements IJpaDemoService {


    private static final Map<String, String> map = new HashMap<String, String>() {
        {
            put("name", "name:EQ");
        }
    };

    @Override
    public boolean isEnableQueryAllRecord() {
        return true;
    }

    @Override
    public Map<String, String> getQueryExpressions() {
        return map;
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

    @Override
    public Page<TbJpaDemo> findByName(String name, Integer page, Integer size) {
        Sort sort = new Sort(Sort.Direction.ASC, "name");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return jpaDemoDao.findAll(pageRequest);
    }

}
