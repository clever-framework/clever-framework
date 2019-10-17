package com.toquery.framework.demo.service.impl;

import com.google.common.collect.Sets;
import com.toquery.framework.demo.dao.IBizJpaNewsRepository;
import com.toquery.framework.demo.dao.IBizJpaNewsSubRepository;
import com.toquery.framework.demo.entity.BizJpaNews;
import com.toquery.framework.demo.entity.BizJpaNewsSub;
import com.toquery.framework.demo.service.IBizJpaNewsService;
import com.toquery.framework.demo.service.IBizJpaNewsSubService;
import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Service
public class BizJpaNewsSubServiceImpl extends AppBaseServiceImpl<Long, BizJpaNewsSub, IBizJpaNewsSubRepository> implements IBizJpaNewsSubService {


    private static final Map<String, String> map = new HashMap<String, String>() {
        {
            put("name", "name:EQ");
            put("num", "num:EQ");
            put("showTime", "showTime:EQ");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return map;
    }


    @Override
    public BizJpaNewsSub getByName(String name) {
        return entityDao.getByName(name);
    }

    @Override
    public BizJpaNewsSub update(Long id, String name) {
        BizJpaNewsSub tbJpaDemo = new BizJpaNewsSub();
        tbJpaDemo.setId(id);
        tbJpaDemo.setName(name);
        return super.update(tbJpaDemo, Sets.newHashSet("name"));
    }

    @Override
    public BizJpaNewsSub getById(Long id) {
        return entityDao.getOne(id);
    }

    @Override
    public Page<BizJpaNewsSub> findByName(String name, Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return entityDao.findAll(pageRequest);
    }

}
