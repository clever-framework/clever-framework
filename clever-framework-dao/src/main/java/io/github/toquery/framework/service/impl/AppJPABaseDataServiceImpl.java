package io.github.toquery.framework.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import io.github.toquery.framework.entity.AppBaseEntityLong;
import io.github.toquery.framework.entity.AppBaseEntityPrimaryKeyLong;
import io.github.toquery.framework.entity.AppJpaSoftDelEntity;
import io.github.toquery.framework.jpa.support.DynamicJPASpecifications;
import io.github.toquery.framework.repository.AppJpaDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Transactional(readOnly = true)
public abstract class AppJPABaseDataServiceImpl<T, D extends AppJpaDataRepository<T, Long>> extends AppBaseDataServiceImpl<T, D, Long> {

    @Resource
    protected D entityDao;


    @Override
    public void preSaveHandler(T entity) {
        super.preSaveHandler(entity);
        //设置记录的创建时间
        if (entity != null && entity instanceof AppBaseEntityLong) {
            AppBaseEntityLong baseEntity = (AppBaseEntityLong) entity;
            baseEntity.setCreateDatetime(new Date());

            //todo 设置记录创建人的id
            /*
            AppAuthPrincipal appAuthPrincipal = appAuthPrincipalService.getAppAuthPrincipal();
            if (appAuthPrincipal != null && appAuthPrincipal.getAuthUser() != null && appAuthPrincipal.getAuthUser().getId() != null) {
                baseEntity.setCreateUserId(appAuthPrincipal.getAuthUser().getId().toString());
            }
            */
        }

    }

    @Override
    public void preUpdateHandler(T entity, Collection<String> updateFields) {
        Assert.notEmpty(updateFields, "必须指定更新的字段");

        super.preUpdateHandler(entity, updateFields);
        //设置记录的更新时间
        if (entity != null && entity instanceof AppBaseEntityLong) {
            AppBaseEntityLong baseEntity = (AppBaseEntityLong) entity;
            baseEntity.setLastUpdateDatetime(new Date());
            updateFields.add("lastUpdateDatetime");

            //todo 设置记录更新人的id
            /*AppAuthPrincipal appAuthPrincipal = appAuthPrincipalService.getAppAuthPrincipal();
            if (appAuthPrincipal != null && appAuthPrincipal.getAuthUser() != null && appAuthPrincipal.getAuthUser().getId() != null) {
                baseEntity.setLastUpdateUserId(appAuthPrincipal.getAuthUser().getId().toString());
                updateFields.add("lastUpdateUserId");
            }*/
        }
    }

    /**
     * 删除后的处理函数
     *
     * @param id
     */
    @Override
    public void postDeleteHandler(Long id) {
        //在进行刷新操作前，先刷新当前实体缓存
        this.entityDao.flush();
    }

    /**
     * 是否是软删除。如果是软删除需要在响应的底层服务中进行相关逻辑处理
     *
     * @return
     */
    public boolean isSoftDel() {
        boolean isSoftDel = ClassUtils.isAssignable(this.entityDao.getDomainClass(), AppJpaSoftDelEntity.class);
        if (isSoftDel) {
            log.info("{} 删除为软删除", this.entityDao.getDomainClass().getName());
        }
        return isSoftDel;
    }

    @Override
    public boolean isExist(Map<String, Object> searchParams) {
        LinkedHashMap<String, Object> queryExpressionMap = formatQueryExpression(searchParams);
        Specification<T> specification = DynamicJPASpecifications.bySearchParam(queryExpressionMap,
                entityDao.getDomainClass());
        return entityDao.count(specification) > 0;
    }

    @Transactional
    @Override
    public void delete(T entity) {
        if (entity == null) {
            return;
        }
        if (entity instanceof AppBaseEntityPrimaryKeyLong) {
            delete(((AppBaseEntityPrimaryKeyLong) entity).getId());
        } else {
            log.error("请根据实体id进行删除。");
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (isBlankId(id)) {
            return;
        }
        if (isSoftDel()) {
            T entity = get(id);
            if (entity != null) {
                //设置软删除
                ((AppJpaSoftDelEntity) entity).setDel(true);
                this.update(entity, Arrays.asList("del"));
            }
        } else {
            super.delete(id);
        }

    }

    @Override
    public long count(Map<String, Object> searchParams) {
        LinkedHashMap<String, Object> queryExpressionMap = formatQueryExpression(searchParams);
        Specification<T> specification = DynamicJPASpecifications.bySearchParam(queryExpressionMap,
                entityDao.getDomainClass());
        return entityDao.count(specification);
    }

    @Override
    public Page<T> queryByPage(Map<String, Object> searchParams, int pageNum, int pageSize, String[] sorts) {
        log.info("获取的原始查询参数->" + JSON.toJSONString(searchParams));

        Specification<T> specification = getQuerySpecification(searchParams);

        Pageable pageable = PageRequest.of(pageNum, pageSize, getSort(sorts));

        return entityDao.findAll(specification, pageable);

//		Page<T> page = entityDao.findAll(specification, pageable) ;
//		//将page对象转换为可以在dubbo中进行序列化和反序列化的分页对象
//		//modified by liupeng , 根据分页查询结果修重新创建分页参数对象。
//		//因为传入的分页参数并不一定都要分页数据
//		return new PageImplInDubbo<T>(page.getContent() ,
//				new PageRequest(page.getNumber() , page.getSize()) , page.getTotalElements()) ;
    }

    @Override
    public List<T> findAll(Map<String, Object> searchParams, String[] sorts) {
        Specification<T> specification = getQuerySpecification(searchParams);
        return entityDao.findAll(specification, getSort(sorts));
    }

    /**
     * 获取查询条件的Specification对象
     *
     * @param searchParams 查询条件map
     * @return
     */
    public Specification<T> getQuerySpecification(Map<String, Object> searchParams) {
        LinkedHashMap<String, Object> queryExpressionMap = formatQueryExpression(searchParams);

        //如果是软删除，默认查询未删除的记录
        if (isSoftDel()) {
            if (queryExpressionMap == null) {
                queryExpressionMap = Maps.newLinkedHashMap();
            }
            if (!queryExpressionMap.containsKey("del:BOOLEANQE")) {
                queryExpressionMap.put("del:BOOLEANQE", false);
                log.info("添加软删除的查询参数 del:BOOLEANQE，查询没有删除的记录。");
            }
        }

        //构建查询条件
        Specification<T> specification = DynamicJPASpecifications.bySearchParam(queryExpressionMap, entityDao.getDomainClass());
        return specification;
    }

    @Override
    protected Sort getSort(String[] sorts) {
        //默认按照创建时间排序
        if ((sorts == null || sorts.length < 1) && AppBaseEntityLong.class.isAssignableFrom(this.entityDao.getDomainClass())) {
            sorts = new String[]{"createDatetime"};
        }
        return super.getSort(sorts);
    }
}
