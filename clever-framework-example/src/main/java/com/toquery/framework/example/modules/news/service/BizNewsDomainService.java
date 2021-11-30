package com.toquery.framework.example.modules.news.service;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Sets;
import com.toquery.framework.example.modules.news.repository.BizNewsRepository;
import com.toquery.framework.example.modules.news.entity.BizNews;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import org.hibernate.Session;
import org.hibernate.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@Service
public class BizNewsDomainService extends AppBaseServiceImpl<BizNews, BizNewsRepository> {


    @PersistenceContext
    private EntityManager entityManager;


    public static final Map<String, String> QUERY_EXPRESSIONS = new HashMap<String, String>() {
        {
            put("num", "num:EQ");
            put("title", "title:EQ");
            put("showTime", "showTime:EQ");
            put("showStatus", "showStatus:EQ");
            put("localDateTime", "localDateTime:EQ");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return QUERY_EXPRESSIONS;
    }

    public List<BizNews> findFilter() {
        Filter filter = entityManager.unwrap(Session.class).enableFilter("gtNum");
        filter.setParameter("showNum", 100);
        filter.setParameter("likeNum", 100);
        List<BizNews> lists = super.dao.findAll();
        entityManager.unwrap(Session.class).disableFilter("gtNum");
        return lists;
    }



    public BizNews update(Long id, String name) {
        BizNews tbJpaDemo = new BizNews();
        tbJpaDemo.setId(id);
        tbJpaDemo.setTitle(name);
        return super.update(tbJpaDemo, Sets.newHashSet("title"));
    }


    public BizNews getById(Long id) {
        return dao.getById(id);
    }


    public Page<BizNews> findByName(String name, Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return dao.findAll(pageRequest);
    }


    public Page<BizNews> queryJpaByPage(int current, int pageSize) {
        return super.dao.findAll(PageRequest.of(current, pageSize));
    }


    public com.github.pagehelper.Page<BizNews> queryMyBatisByPage(int current, int pageSize) {
        com.github.pagehelper.Page<BizNews> bizNewsPage = PageHelper.startPage(current, pageSize);
        super.dao.findMyBatisByTitle(null);
        return bizNewsPage;
    }


    public List<BizNews> findJpa() {
        return super.dao.findAll();
    }


    public List<BizNews> findMyBatis() {
        return super.dao.findMyBatisByTitle(null);
    }


    public BizNews saveJpa(BizNews bizNews) {
        return super.dao.save(bizNews);
    }


    public BizNews saveMyBatis(BizNews bizNews) {
        super.dao.saveMyBatis(bizNews);
        return super.getById(bizNews.getId());
    }


    public BizNews updateJpa(BizNews bizNews) {
        return super.dao.saveAndFlush(bizNews);
    }


    public BizNews updateMyBatis(BizNews bizNews) {
        return super.dao.updateMyBatis(bizNews);
    }


    public void deleteJpa(Set<Long> ids) {
        super.dao.deleteJpaIds(ids);
    }


    public void deleteMyBatis(Set<Long> ids) {
        super.dao.deleteMyBatis(ids);
    }


    public BizNews detailJpa(Long id) {
        return super.dao.findJpaById(id);
    }


    public BizNews detailMyBatis(Long id) {
        return super.dao.findMyBatisById(id);
    }

}
