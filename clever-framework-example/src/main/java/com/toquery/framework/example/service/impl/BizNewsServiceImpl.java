package com.toquery.framework.example.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Sets;
import com.toquery.framework.example.dao.IBizNewsRepository;
import com.toquery.framework.example.entity.BizNews;
import com.toquery.framework.example.service.IBizNewsService;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@Service
public class BizNewsServiceImpl extends AppBaseServiceImpl<Long, BizNews, IBizNewsRepository> implements IBizNewsService {


    private static final Map<String, String> map = new HashMap<String, String>() {
        {
            put("title", "name:EQ");
            put("num", "num:EQ");
            put("showTime", "showTime:EQ");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return map;
    }


    @Override
    public BizNews update(Long id, String name) {
        BizNews tbJpaDemo = new BizNews();
        tbJpaDemo.setId(id);
        tbJpaDemo.setTitle(name);
        return super.update(tbJpaDemo, Sets.newHashSet("title"));
    }

    @Override
    public BizNews getById(Long id) {
        return dao.getOne(id);
    }

    @Override
    public Page<BizNews> findByName(String name, Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return dao.findAll(pageRequest);
    }

    @Override
    public Page<BizNews> queryJpaByPage(int pageNum, int pageSize) {
        return super.dao.findAll(PageRequest.of(pageNum, pageSize));
    }

    @Override
    public com.github.pagehelper.Page<BizNews> queryMyBatisByPage(int pageNum, int pageSize) {
        com.github.pagehelper.Page<BizNews> bizNewsPage = PageHelper.startPage(pageNum, pageSize);
        super.dao.findMyBatisByTitle(null);
        return bizNewsPage;
    }

    @Override
    public List<BizNews> listJpa() {
        return super.dao.findAll();
    }

    @Override
    public List<BizNews> listMyBatis() {
        return super.dao.findMyBatisByTitle(null);
    }

    @Override
    public BizNews saveJpa(BizNews bizNews) {
        return super.dao.save(bizNews);
    }

    @Override
    public BizNews saveMyBatis(BizNews bizNews) {
        super.dao.saveMyBatis(bizNews);
        return super.getById(bizNews.getId());
    }

    @Override
    public BizNews updateJpa(BizNews bizNews) {
        return super.dao.saveAndFlush(bizNews);
    }

    @Override
    public BizNews updateMyBatis(BizNews bizNews) {
        return super.dao.updateMyBatis(bizNews);
    }

    @Override
    public void deleteJpa(Set<Long> ids) {
        super.dao.deleteJpaIds(ids);
    }

    @Override
    public void deleteMyBatis(Set<Long> ids) {
        super.dao.deleteMyBatis(ids);
    }

    @Override
    public BizNews detailJpa(Long id) {
        return super.dao.findJpaById(id);
    }

    @Override
    public BizNews detailMyBatis(Long id) {
        return super.dao.findMyBatisById(id);
    }

}
