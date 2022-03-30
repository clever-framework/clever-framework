package com.toquery.framework.example.bff.admin.news.info.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Sets;
import com.toquery.framework.example.bff.admin.news.info.dao.BizNewsDao;
import com.toquery.framework.example.bff.admin.news.info.dao.BizNewsMapper;
import com.toquery.framework.example.bff.admin.news.info.model.constant.QueryType;
import com.toquery.framework.example.bff.admin.news.info.model.mapper.BizNewsModelMapper;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsAddRequest;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsListRequest;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsPageRequest;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsUpdateRequest;
import com.toquery.framework.example.bff.admin.news.info.model.response.BizNewsInfoResponse;
import com.toquery.framework.example.bff.admin.news.info.model.response.BizNewsListResponse;
import com.toquery.framework.example.modules.news.info.entity.BizNews;
import com.toquery.framework.example.modules.news.info.service.BizNewsDomainService;
import io.github.toquery.framework.crud.service.impl.AppBFFServiceImpl;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import lombok.AllArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
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
 *
 */
@Service
@AllArgsConstructor
public class BizNewsService extends AppBFFServiceImpl<BizNews, BizNewsDao> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BizNewsMapper bizNewsMapper;

    @Autowired
    private BizNewsDomainService bizNewsDomainService;


    public static final Map<String, String> QUERY_EXPRESSIONS = new HashMap<String, String>(BizNewsDomainService.QUERY_EXPRESSIONS) {
        {
            put("titleLIKE", "title:LIKE");
            put("showTimeGT", "showTime:GT");
            put("showTimeLT", "showTime:LT");
            put("showTimeGE", "showTime:GE");
            put("showTimeLE", "showTime:LE");

            put("localDateTimeGT", "localDateTime:GT");
            put("localDateTimeLT", "localDateTime:LT");
            put("localDateTimeGE", "localDateTime:GE");
            put("localDateTimeLE", "localDateTime:LE");
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
        List<BizNews> lists = repository.findAll();
        entityManager.unwrap(Session.class).disableFilter("gtNum");
        return lists;
    }


    public BizNews update(Long id, String name) {
        BizNews tbJpaDemo = new BizNews();
        tbJpaDemo.setId(id);
        tbJpaDemo.setTitle(name);
        return bizNewsDomainService.update(tbJpaDemo, Sets.newHashSet("title"));
    }

    public Page<BizNews> findByName(String name, Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repository.findAll(pageRequest);
    }


    public Page<BizNews> queryJpaByPage(int current, int pageSize) {
        return repository.findAll(PageRequest.of(current, pageSize));
    }



    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<BizNews> queryMyBatisPlusByPage(int current, int pageSize) {
        Wrapper<BizNews> queryWrapper = new LambdaQueryWrapper<>();
        return bizNewsMapper.selectPage(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<BizNews>(current, pageSize), queryWrapper);
    }


    public List<BizNews> findJpa() {
        return repository.findAll();
    }


    public List<BizNews> findMyBatis() {
        return bizNewsMapper.listMyBatis();
    }

    private List<BizNews> findMyBatisPlus() {
//        return repository.selectList(null);
        return null;
    }


    public BizNews saveJpa(BizNews bizNews) {
        return repository.save(bizNews);
    }


    public BizNews saveMyBatis(BizNews bizNews) {
        bizNewsMapper.saveMyBatis(bizNews);
        return bizNewsDomainService.getById(bizNews.getId());
    }


    private BizNews saveMyBatisPlus(BizNews bizNews) {
//        repository.insert(bizNews);
        return bizNewsDomainService.getById(bizNews.getId());
    }


    public BizNews updateJpa(BizNews bizNews) {
        return repository.saveAndFlush(bizNews);
    }


    public BizNews updateMyBatis(BizNews bizNews) {
        return bizNewsMapper.updateMyBatis(bizNews);
    }

    private BizNews updateMyBatisPlus(BizNews bizNews) {
        LambdaUpdateWrapper<BizNews> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BizNews::getTitle, bizNews.getTitle() + "-LambdaUpdateWrapper");
//        repository.update(bizNews, updateWrapper);
        return bizNewsDomainService.getById(bizNews.getId());
    }


    public void deleteJpa(Set<Long> ids) {
        repository.deleteJpaIds(ids);
    }


    public void deleteMyBatis(Set<Long> ids) {
        bizNewsMapper.deleteMyBatis(ids);
    }

    private void deleteMyBatisPlus(Set<Long> ids) {
//        ids.forEach(id -> repository.removeById(id));
    }


    public BizNews detailJpa(Long id) {
        return repository.findJpaById(id);
    }

    public BizNews detailMyBatis(Long id) {
        return bizNewsMapper.getMyBatisById(id);
    }

    private BizNews detailMyBatisPlus(Long id) {
        return bizNewsMapper.getMyBatisById(id);
    }

    public BizNewsInfoResponse save(QueryType queryType, BizNewsAddRequest request) {
        BizNews bizNews = null;
        switch (queryType) {
            case APP: {
                bizNews = bizNewsDomainService.save(BizNewsModelMapper.INSTANCE.add2BizNews(request));
                break;
            }
            case JPA: {
                bizNews = this.saveJpa(BizNewsModelMapper.INSTANCE.add2BizNews(request));
                break;
            }
            case MYBATIS: {
                bizNews = this.saveMyBatis(BizNewsModelMapper.INSTANCE.add2BizNews(request));
                break;
            }
            case MYBATIS_PLUS: {
                bizNews = this.saveMyBatisPlus(BizNewsModelMapper.INSTANCE.add2BizNews(request));
                break;
            }
        }

        return BizNewsModelMapper.INSTANCE.bizNews2Info(bizNews);
    }


    public BizNewsInfoResponse update(QueryType queryType, BizNewsUpdateRequest request) {
        BizNews bizNews = null;
        switch (queryType) {
            case APP: {
                bizNews = bizNewsDomainService.update(BizNewsModelMapper.INSTANCE.update2BizNews(request));
                break;
            }
            case JPA: {
                bizNews = this.updateJpa(BizNewsModelMapper.INSTANCE.update2BizNews(request));
                break;
            }
            case MYBATIS: {
                bizNews = this.updateMyBatis(BizNewsModelMapper.INSTANCE.update2BizNews(request));
                break;
            }
            case MYBATIS_PLUS: {
                bizNews = this.updateMyBatisPlus(BizNewsModelMapper.INSTANCE.update2BizNews(request));
                break;
            }
        }
        return BizNewsModelMapper.INSTANCE.bizNews2Info(bizNews);
    }


    public ResponseBodyWrap<?> pageMultiType(BizNewsPageRequest bizNewsPageRequest) {
        ResponseBodyWrap<?> responseParam = null;
        switch (bizNewsPageRequest.getQueryType()) {
            case APP: {
                org.springframework.data.domain.Page<BizNews> bizNewsPage = bizNewsDomainService.page(bizNewsPageRequest.getCurrent(), bizNewsPageRequest.getPageSize());
                responseParam = ResponseBodyWrap.builder().page(bizNewsPage).build();
                break;
            }
            case JPA: {
                org.springframework.data.domain.Page<BizNews> bizNewsPage = this.queryJpaByPage(bizNewsPageRequest.getCurrent(), bizNewsPageRequest.getPageSize());
                responseParam = ResponseBodyWrap.builder().page(bizNewsPage).build();
                break;
            }
            case MYBATIS: {

            }
            case MYBATIS_PLUS: {
                com.baomidou.mybatisplus.extension.plugins.pagination.Page<BizNews> bizNewsPage = this.queryMyBatisPlusByPage(bizNewsPageRequest.getCurrent(), bizNewsPageRequest.getPageSize());
                responseParam = ResponseBodyWrap.builder().page(bizNewsPage).build();
                break;
            }
        }
        return responseParam;
    }

    public List<BizNewsListResponse> list(BizNewsListRequest bizNewsListRequest) {
        List<BizNewsListResponse> bizNewsListResponses = null;
        switch (bizNewsListRequest.getQueryType()) {
            case APP: {
                List<BizNews> bizNewsList = bizNewsDomainService.list();
                bizNewsListResponses = BizNewsModelMapper.INSTANCE.bizNews2List(bizNewsList);
                break;
            }
            case JPA: {
                List<BizNews> bizNewsList = this.findJpa();
                bizNewsListResponses = BizNewsModelMapper.INSTANCE.bizNews2List(bizNewsList);
                break;
            }
            case FILTER: {
                List<BizNews> bizNewsList = this.findFilter();
                bizNewsListResponses = BizNewsModelMapper.INSTANCE.bizNews2List(bizNewsList);
                break;
            }
            case MYBATIS: {
                List<BizNews> bizNewsList = this.findMyBatis();
                bizNewsListResponses = BizNewsModelMapper.INSTANCE.bizNews2List(bizNewsList);
                break;
            }
            case MYBATIS_PLUS: {
                List<BizNews> bizNewsList = this.findMyBatisPlus();
                bizNewsListResponses = BizNewsModelMapper.INSTANCE.bizNews2List(bizNewsList);
                break;
            }
        }
        return bizNewsListResponses;
    }


    public void delete(QueryType queryType, Set<Long> ids) {
        switch (queryType) {
            case APP: {
                bizNewsDomainService.deleteByIds(ids);
                break;
            }
            case JPA: {
                this.deleteJpa(ids);
                break;
            }
            case MYBATIS: {
                this.deleteMyBatis(ids);
                break;
            }
            case MYBATIS_PLUS: {
                this.deleteMyBatisPlus(ids);
                break;
            }
        }
    }


    public BizNewsInfoResponse detail(QueryType queryType, Long id) {
        BizNews bizNews = null;
        switch (queryType) {
            case APP: {
                bizNews = bizNewsDomainService.getById(id);
                break;
            }
            case JPA: {
                bizNews = this.detailJpa(id);
                break;
            }
            case MYBATIS: {
                bizNews = this.detailMyBatis(id);
                break;
            }
            case MYBATIS_PLUS: {
                bizNews = this.detailMyBatisPlus(id);
                break;
            }
        }
        return BizNewsModelMapper.INSTANCE.bizNews2Info(bizNews);
    }


    public Page<BizNews> page(BizNewsPageRequest bizNewsPageRequest) {
        Map<String, Object> params = BizNewsModelMapper.INSTANCE.page2Map(bizNewsPageRequest);
        return bizNewsDomainService.page(params, bizNewsPageRequest.getCurrent(), bizNewsPageRequest.getPageSize());
    }
}
