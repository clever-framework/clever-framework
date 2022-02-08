package com.toquery.framework.example.bff.admin.author.info.service;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Sets;
import com.toquery.framework.example.bff.admin.author.info.dao.BizAuthorDao;
import com.toquery.framework.example.bff.admin.author.info.model.constant.QueryType;
import com.toquery.framework.example.bff.admin.author.info.model.mapper.BizAuthorMapper;
import com.toquery.framework.example.bff.admin.author.info.model.request.BizAuthorAddRequest;
import com.toquery.framework.example.bff.admin.author.info.model.request.BizAuthorListRequest;
import com.toquery.framework.example.bff.admin.author.info.model.request.BizAuthorPageRequest;
import com.toquery.framework.example.bff.admin.author.info.model.request.BizAuthorUpdateRequest;
import com.toquery.framework.example.bff.admin.author.info.model.response.BizAuthorInfoResponse;
import com.toquery.framework.example.bff.admin.author.info.model.response.BizAuthorListResponse;
import com.toquery.framework.example.modules.author.info.entity.BizAuthor;
import com.toquery.framework.example.modules.author.info.service.BizAuthorDomainService;
import io.github.toquery.framework.crud.service.impl.AppBFFServiceImpl;
//import io.github.toquery.framework.datasource.DataSource;
import io.github.toquery.framework.datasource.DataSourceSwitch;
import io.github.toquery.framework.web.domain.ResponseBody;
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
@DataSourceSwitch(value = "default")
@Service
@AllArgsConstructor
public class BizAuthorService extends AppBFFServiceImpl<BizAuthor, BizAuthorDao> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BizAuthorDomainService bizAuthorDomainService;


    public static final Map<String, String> QUERY_EXPRESSIONS = new HashMap<String, String>(BizAuthorDomainService.QUERY_EXPRESSIONS) {
        {
            put("authorName", "authorName:EQ");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return QUERY_EXPRESSIONS;
    }

    public List<BizAuthor> findFilter() {
        Filter filter = entityManager.unwrap(Session.class).enableFilter("gtNum");
        filter.setParameter("showNum", 100);
        filter.setParameter("likeNum", 100);
        List<BizAuthor> lists = repository.findAll();
        entityManager.unwrap(Session.class).disableFilter("gtNum");
        return lists;
    }


    public BizAuthor update(Long id, String name) {
        BizAuthor tbJpaDemo = new BizAuthor();
        tbJpaDemo.setId(id);
        tbJpaDemo.setAuthorName(name);
        return bizAuthorDomainService.update(tbJpaDemo, Sets.newHashSet("authorName"));
    }

    public Page<BizAuthor> findByName(String name, Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repository.findAll(pageRequest);
    }


    public Page<BizAuthor> queryJpaByPage(int current, int pageSize) {
        return repository.findAll(PageRequest.of(current, pageSize));
    }


    public com.github.pagehelper.Page<BizAuthor> queryMyBatisByPage(int current, int pageSize) {
        com.github.pagehelper.Page<BizAuthor> bizAuthorPage = PageHelper.startPage(current, pageSize);
        repository.findByAuthorNameLike(null);
        return bizAuthorPage;
    }


    public List<BizAuthor> findJpa() {
        return repository.findAll();
    }


    public List<BizAuthor> findMyBatis() {
        return repository.findMyBatisByAuthorName(null);
    }


    public BizAuthor saveJpa(BizAuthor bizAuthor) {
        return repository.save(bizAuthor);
    }


    public BizAuthor saveMyBatis(BizAuthor bizAuthor) {
        repository.saveMyBatis(bizAuthor);
        return bizAuthorDomainService.getById(bizAuthor.getId());
    }


    public BizAuthor updateJpa(BizAuthor bizAuthor) {
        return repository.saveAndFlush(bizAuthor);
    }


    public BizAuthor updateMyBatis(BizAuthor bizAuthor) {
        return repository.updateMyBatis(bizAuthor);
    }


    public void deleteJpa(Set<Long> ids) {
        repository.deleteJpaIds(ids);
    }


    public void deleteMyBatis(Set<Long> ids) {
        repository.deleteMyBatis(ids);
    }


    public BizAuthor detailJpa(Long id) {
        return repository.findJpaById(id);
    }

    public BizAuthor detailMyBatis(Long id) {
        return repository.findMyBatisById(id);
    }

    public BizAuthorInfoResponse save(QueryType queryType, BizAuthorAddRequest request) {
        BizAuthor bizAuthor = null;
        switch (queryType) {
            case APP: {
                bizAuthor = bizAuthorDomainService.save(BizAuthorMapper.INSTANCE.add2BizAuthor(request));
                break;
            }
            case JPA: {
                bizAuthor = this.saveJpa(BizAuthorMapper.INSTANCE.add2BizAuthor(request));
                break;
            }
            case MYBATIS: {
                bizAuthor = this.saveMyBatis(BizAuthorMapper.INSTANCE.add2BizAuthor(request));
                break;
            }
        }

        return BizAuthorMapper.INSTANCE.bizAuthor2Info(bizAuthor);
    }

    public BizAuthorInfoResponse update(QueryType queryType, BizAuthorUpdateRequest request) {
        BizAuthor bizAuthor = null;
        switch (queryType) {
            case APP: {
                bizAuthor = bizAuthorDomainService.update(BizAuthorMapper.INSTANCE.update2BizAuthor(request));
                break;
            }
            case JPA: {
                bizAuthor = this.updateJpa(BizAuthorMapper.INSTANCE.update2BizAuthor(request));
                break;
            }
            case MYBATIS: {
                bizAuthor = this.updateMyBatis(BizAuthorMapper.INSTANCE.update2BizAuthor(request));
                break;
            }
        }
        return BizAuthorMapper.INSTANCE.bizAuthor2Info(bizAuthor);
    }

    public ResponseBody<?> pageMultiType(BizAuthorPageRequest bizAuthorPageRequest) {
        ResponseBody<?> responseParam = null;
        switch (bizAuthorPageRequest.getQueryType()) {
            case APP: {
                Page<BizAuthor> bizAuthorPage = bizAuthorDomainService.page(bizAuthorPageRequest.getCurrent(), bizAuthorPageRequest.getPageSize());
                responseParam = ResponseBody.builder().page(bizAuthorPage).build();
                break;
            }
            case JPA: {
                Page<BizAuthor> bizAuthorPage = this.queryJpaByPage(bizAuthorPageRequest.getCurrent(), bizAuthorPageRequest.getPageSize());
                responseParam = ResponseBody.builder().page(bizAuthorPage).build();
                break;
            }
            case MYBATIS: {
                com.github.pagehelper.Page<BizAuthor> bizAuthorPage = this.queryMyBatisByPage(bizAuthorPageRequest.getCurrent(), bizAuthorPageRequest.getPageSize());
                responseParam = ResponseBody.builder().page(bizAuthorPage).build();
                break;
            }
        }
        return responseParam;
    }

    public List<BizAuthorListResponse> list(BizAuthorListRequest bizAuthorListRequest) {
        List<BizAuthorListResponse> bizAuthorListResponses = null;
        switch (bizAuthorListRequest.getQueryType()) {
            case APP: {
                List<BizAuthor> bizAuthorList = bizAuthorDomainService.list();
                bizAuthorListResponses = BizAuthorMapper.INSTANCE.bizAuthor2List(bizAuthorList);
                break;
            }
            case JPA: {
                List<BizAuthor> bizAuthorList = this.findJpa();
                bizAuthorListResponses = BizAuthorMapper.INSTANCE.bizAuthor2List(bizAuthorList);
                break;
            }
            case FILTER: {
                List<BizAuthor> bizAuthorList = this.findFilter();
                bizAuthorListResponses = BizAuthorMapper.INSTANCE.bizAuthor2List(bizAuthorList);
                break;
            }
            case MYBATIS: {
                List<BizAuthor> bizAuthorList = this.findMyBatis();
                bizAuthorListResponses = BizAuthorMapper.INSTANCE.bizAuthor2List(bizAuthorList);
                break;
            }
        }
        return bizAuthorListResponses;
    }

    public void delete(QueryType queryType, Set<Long> ids) {
        switch (queryType) {
            case APP: {
                bizAuthorDomainService.deleteByIds(ids);
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

    public BizAuthorInfoResponse detail(QueryType queryType, Long id) {
        BizAuthor bizAuthor = null;
        switch (queryType) {
            case APP: {
                bizAuthor = bizAuthorDomainService.getById(id);
                break;
            }
            case JPA: {
                bizAuthor = this.detailJpa(id);
                break;
            }
            case MYBATIS: {
                bizAuthor = this.detailMyBatis(id);
                break;
            }
        }
        return BizAuthorMapper.INSTANCE.bizAuthor2Info(bizAuthor);
    }

    @DataSourceSwitch(value = "default")
    public Page<BizAuthor> page(BizAuthorPageRequest bizAuthorPageRequest) {
        Map<String, Object> params = BizAuthorMapper.INSTANCE.page2Map(bizAuthorPageRequest);
        return bizAuthorDomainService.page(params, bizAuthorPageRequest.getCurrent(), bizAuthorPageRequest.getPageSize());
    }
}
