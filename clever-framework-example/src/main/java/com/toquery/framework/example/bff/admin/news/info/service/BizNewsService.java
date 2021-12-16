package com.toquery.framework.example.bff.admin.news.info.service;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Sets;
import com.toquery.framework.example.bff.admin.news.info.dao.BizNewsDao;
import com.toquery.framework.example.bff.admin.news.info.model.constant.QueryType;
import com.toquery.framework.example.bff.admin.news.info.model.mapper.BizNewsMapper;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsAddRequest;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsListRequest;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsPageRequest;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsUpdateRequest;
import com.toquery.framework.example.bff.admin.news.info.model.response.BizNewsInfoResponse;
import com.toquery.framework.example.bff.admin.news.info.model.response.BizNewsListResponse;
import com.toquery.framework.example.modules.news.info.entity.BizNews;
import com.toquery.framework.example.modules.news.info.service.BizNewsDomainService;
import io.github.toquery.framework.crud.service.impl.AppBFFServiceImpl;
import io.github.toquery.framework.webmvc.domain.ResponseResult;
import org.hibernate.Filter;
import org.hibernate.Session;
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
public class BizNewsService extends AppBFFServiceImpl {

    private BizNewsDao bizNewsDao;

    @PersistenceContext
    private EntityManager entityManager;

    private BizNewsDomainService bizNewsDomainService;

    public BizNewsService(BizNewsDao bizNewsDao, BizNewsDomainService bizNewsDomainService) {
        this.bizNewsDao = bizNewsDao;
        this.bizNewsDomainService = bizNewsDomainService;
    }


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
        List<BizNews> lists = bizNewsDao.findAll();
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
        return bizNewsDao.findAll(pageRequest);
    }


    public Page<BizNews> queryJpaByPage(int current, int pageSize) {
        return bizNewsDao.findAll(PageRequest.of(current, pageSize));
    }


    public com.github.pagehelper.Page<BizNews> queryMyBatisByPage(int current, int pageSize) {
        com.github.pagehelper.Page<BizNews> bizNewsPage = PageHelper.startPage(current, pageSize);
        bizNewsDao.findMyBatisByTitle(null);
        return bizNewsPage;
    }


    public List<BizNews> findJpa() {
        return bizNewsDao.findAll();
    }


    public List<BizNews> findMyBatis() {
        return bizNewsDao.findMyBatisByTitle(null);
    }


    public BizNews saveJpa(BizNews bizNews) {
        return bizNewsDao.save(bizNews);
    }


    public BizNews saveMyBatis(BizNews bizNews) {
        bizNewsDao.saveMyBatis(bizNews);
        return bizNewsDomainService.getById(bizNews.getId());
    }


    public BizNews updateJpa(BizNews bizNews) {
        return bizNewsDao.saveAndFlush(bizNews);
    }


    public BizNews updateMyBatis(BizNews bizNews) {
        return bizNewsDao.updateMyBatis(bizNews);
    }


    public void deleteJpa(Set<Long> ids) {
        bizNewsDao.deleteJpaIds(ids);
    }


    public void deleteMyBatis(Set<Long> ids) {
        bizNewsDao.deleteMyBatis(ids);
    }


    public BizNews detailJpa(Long id) {
        return bizNewsDao.findJpaById(id);
    }

    public BizNews detailMyBatis(Long id) {
        return bizNewsDao.findMyBatisById(id);
    }

    public BizNewsInfoResponse save(QueryType queryType, BizNewsAddRequest request) {
        BizNews bizNews = null;
        switch (queryType) {
            case APP: {
                bizNews = bizNewsDomainService.save(BizNewsMapper.INSTANCE.add2BizNews(request));
                break;
            }
            case JPA: {
                bizNews = this.saveJpa(BizNewsMapper.INSTANCE.add2BizNews(request));
                break;
            }
            case MYBATIS: {
                bizNews = this.saveMyBatis(BizNewsMapper.INSTANCE.add2BizNews(request));
                break;
            }
        }

        return BizNewsMapper.INSTANCE.bizNews2Info(bizNews);
    }

    public BizNewsInfoResponse update(QueryType queryType, BizNewsUpdateRequest request) {
        BizNews bizNews = null;
        switch (queryType) {
            case APP: {
                bizNews = bizNewsDomainService.update(BizNewsMapper.INSTANCE.update2BizNews(request));
                break;
            }
            case JPA: {
                bizNews = this.updateJpa(BizNewsMapper.INSTANCE.update2BizNews(request));
                break;
            }
            case MYBATIS: {
                bizNews = this.updateMyBatis(BizNewsMapper.INSTANCE.update2BizNews(request));
                break;
            }
        }
        return BizNewsMapper.INSTANCE.bizNews2Info(bizNews);
    }

    public ResponseResult pageMultiType(BizNewsPageRequest bizNewsPageRequest) {
        ResponseResult responseParam = null;
        switch (bizNewsPageRequest.getQueryType()) {
            case APP: {
                org.springframework.data.domain.Page<BizNews> bizNewsPage = bizNewsDomainService.page(bizNewsPageRequest.getCurrent(), bizNewsPageRequest.getPageSize());
                responseParam = ResponseResult.builder().page(bizNewsPage).build();
                break;
            }
            case JPA: {
                org.springframework.data.domain.Page<BizNews> bizNewsPage = this.queryJpaByPage(bizNewsPageRequest.getCurrent(), bizNewsPageRequest.getPageSize());
                responseParam = ResponseResult.builder().page(bizNewsPage).build();
                break;
            }
            case MYBATIS: {
                com.github.pagehelper.Page<BizNews> bizNewsPage = this.queryMyBatisByPage(bizNewsPageRequest.getCurrent(), bizNewsPageRequest.getPageSize());
                responseParam = ResponseResult.builder().page(bizNewsPage).build();
                break;
            }
        }
        return responseParam;
    }

    public List<BizNewsListResponse> list(BizNewsListRequest bizNewsListRequest) {
        List<BizNewsListResponse> bizNewsListResponses = null;
        switch (bizNewsListRequest.getQueryType()) {
            case APP: {
                List<BizNews> bizNewsList = bizNewsDomainService.find();
                bizNewsListResponses = BizNewsMapper.INSTANCE.bizNews2List(bizNewsList);
                break;
            }
            case JPA: {
                List<BizNews> bizNewsList = this.findJpa();
                bizNewsListResponses = BizNewsMapper.INSTANCE.bizNews2List(bizNewsList);
                break;
            }
            case FILTER: {
                List<BizNews> bizNewsList = this.findFilter();
                bizNewsListResponses = BizNewsMapper.INSTANCE.bizNews2List(bizNewsList);
                break;
            }
            case MYBATIS: {
                List<BizNews> bizNewsList = this.findMyBatis();
                bizNewsListResponses = BizNewsMapper.INSTANCE.bizNews2List(bizNewsList);
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
        }
        return BizNewsMapper.INSTANCE.bizNews2Info(bizNews);
    }

    public Page<BizNews> page(BizNewsPageRequest bizNewsPageRequest) {
        Map<String, Object> params = BizNewsMapper.INSTANCE.page2Map(bizNewsPageRequest);
        return bizNewsDomainService.page(params, bizNewsPageRequest.getCurrent(), bizNewsPageRequest.getPageSize());
    }
}
