package io.github.toquery.framework.curd.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.curd.service.AppBaseService;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.entity.AppBaseEntityJpaSoftDelEntity;
import io.github.toquery.framework.dao.jpa.support.DynamicJPASpecifications;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ClassUtils;
import org.reflections.ReflectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * jpa快速curd方法
 *
 * @param <ID> 主键类型
 * @param <T>  实体类型
 * @param <D>  Dao操作类
 */
@Slf4j
public abstract class AppBaseServiceImpl<ID extends Serializable, T extends AppBaseEntity, D extends AppJpaBaseRepository<T, ID>> implements AppBaseService<T, ID> {

    @Resource
    protected D entityDao;


    @Override
    @Transactional
    public T save(T entity) {
        preSaveHandler(entity);
        T t = baseEntityDao.save(entity);
        postSaveHandler(entity);
        return t;
    }

    /**
     * 保存之前的处理操作
     */
    protected void preSaveHandler(T entity) {
        //设置记录的创建时间
        entity.setCreateDatetime(new Date());
        //todo 设置记录创建人的id
            /*
            AppAuthPrincipal appAuthPrincipal = appAuthPrincipalService.getAppAuthPrincipal();
            if (appAuthPrincipal != null && appAuthPrincipal.getAuthUser() != null && appAuthPrincipal.getAuthUser().getId() != null) {
                baseEntity.setCreateUserId(appAuthPrincipal.getAuthUser().getId().toString());
            }
            */
    }

    /**
     * 保存之后的处理操作
     */
    protected void postSaveHandler(T entity) {

    }

    @Override
    @Transactional
    public Iterable<T> saveBatch(Iterable<T> entityIterable) {
        preSaveBatchHandler(entityIterable);
        Iterable<T> saveData = baseEntityDao.saveAll(entityIterable);
        postSaveBatchHandler(entityIterable);
        return saveData;
    }

    /**
     * 保存之前的预处理操作
     */
    protected void preSaveBatchHandler(Iterable<T> entityIterable) {

    }


    /**
     * 保存之后的处理操作
     */
    protected void postSaveBatchHandler(Iterable<T> entityIterable) {

    }

    /**
     * 根据id删除记录，删除时和当前对象的关联关系会被同步删除
     */
    @Override
    @Transactional
    public void deleteById(ID id) {
        if (isBlankId(id)) {
            return;
        }
        postDeleteHandler(id);
        if (isSoftDel()) {
            T entity = getById(id);
            if (entity != null) {
                //设置软删除
                ((AppBaseEntityJpaSoftDelEntity) entity).setDel(true);
                this.update(entity, Arrays.asList("del"));
            }
        } else {
            baseEntityDao.deleteById(id);
        }
    }

    public void postDeleteHandler(ID id) {
        //在进行刷新操作前，先刷新当前实体缓存
        this.entityDao.flush();
    }

    /**
     * 批量删除记录，删除时和当前对象的关联关系会被同步删除
     */
    @Override
    @Transactional
    public void deleteByIds(Iterable<ID> ids) {
        for (ID id : ids) {
            if (isBlankId(id)) {
                continue;
            }
            deleteById(id);
        }
    }


    /**
     * 更新之前的预处理操作
     */
    public void preUpdateHandler(T entity, Collection<String> updateFields) {
        Assert.notEmpty(updateFields, "必须指定更新的字段");

        //设置记录的更新时间
        entity.setLastUpdateDatetime(new Date());
        updateFields.add("lastUpdateDatetime");

        //todo 设置记录更新人的id
            /*AppAuthPrincipal appAuthPrincipal = appAuthPrincipalService.getAppAuthPrincipal();
            if (appAuthPrincipal != null && appAuthPrincipal.getAuthUser() != null && appAuthPrincipal.getAuthUser().getId() != null) {
                baseEntity.setLastUpdateUserId(appAuthPrincipal.getAuthUser().getId().toString());
                updateFields.add("lastUpdateUserId");
            }*/


        //获取当前实体的所有属性
        if (entityFields == null) {
            entityFields = Sets.newHashSet();
            for (Field field : ReflectionUtils.getAllFields(entity.getClass())) {
                //过滤掉需要映射的jpa字段
                Transient fieldTransient = field.getAnnotation(Transient.class);
                if (fieldTransient != null) {
                    continue;
                }

                entityFields.add(field.getName());
            }
        }

        //检查更新实体的属性，如果不是实体的属性，则自动移除
        if (updateFields != null) {
            List<String> noUpdateFields = null;
            for (String updateField : updateFields) {
                if (entityFields.contains(updateField)) {
                    continue;
                }
                //记录不需要更新的字段
                if (noUpdateFields == null) {
                    noUpdateFields = Lists.newArrayList();
                }
                noUpdateFields.add(updateField);
            }

            if (noUpdateFields != null) {
                updateFields.removeAll(noUpdateFields);
                log.info("更新之前移除 {} 中不包含的属性 {}", entity.getClass().getName(), Joiner.on(",").join(noUpdateFields));
            }

        } else {
            //如果没有指定更新字段，默认为全部字段
            if (updateFields != null && updateFields.size() == 0) {
                updateFields.addAll(entityFields);
            }
        }

        //移除id字段，不更新id
        if (updateFields != null) {
            updateFields.remove("primary");
        }
    }


    /**
     * 是否是软删除。如果是软删除需要在响应的底层服务中进行相关逻辑处理
     *
     * @return
     */
    public boolean isSoftDel() {
        boolean isSoftDel = ClassUtils.isAssignable(this.entityDao.getDomainClass(), AppBaseEntityJpaSoftDelEntity.class);
        if (isSoftDel) {
            log.info("{} 删除为软删除", this.entityDao.getDomainClass().getName());
        }
        return isSoftDel;
    }

    @Override
    public boolean existsById(Map<String, Object> searchParams) {
        LinkedHashMap<String, Object> queryExpressionMap = formatQueryExpression(searchParams);
        Specification<T> specification = DynamicJPASpecifications.bySearchParam(queryExpressionMap, entityDao.getDomainClass());
        return entityDao.count(specification) > 0;
    }


    @Override
    public long count(Map<String, Object> searchParams) {
        LinkedHashMap<String, Object> queryExpressionMap = formatQueryExpression(searchParams);
        Specification<T> specification = DynamicJPASpecifications.bySearchParam(queryExpressionMap, entityDao.getDomainClass());
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
    public List<T> find(Map<String, Object> searchParams, String[] sorts) {
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
        return DynamicJPASpecifications.bySearchParam(queryExpressionMap, entityDao.getDomainClass());
    }

    /**
     * 获取排序类型，sorts中元素结构为："字段名称"或"字段名称_asc"或"字段名称_desc"，如果不带排序方式则默认为升序。<br>
     * 其中下划线也可以改为“:”
     */
    protected Sort getSort(String[] sorts) {
        //默认按照创建时间排序
        if ((sorts == null || sorts.length < 1) && AppBaseEntity.class.isAssignableFrom(this.entityDao.getDomainClass())) {
            sorts = new String[]{"createDatetime"};
        }

        //没有指定排序字段，则返回未排序的对象
        if (sorts == null || sorts.length < 1) {
            return Sort.unsorted();
        }
        Sort sort = null;
        int sortTypeIndex = -1;
        String sortType = null;
        Sort newSort = null;
        for (String sortStr : sorts) {
            if (Strings.isNullOrEmpty(sortStr)) {
                continue;
            }

            //定位排序字段和排序方式的分隔符，优先使用“:”然后使用“_”
            sortTypeIndex = sortStr.lastIndexOf(':');
            if (sortTypeIndex == -1) {
                sortTypeIndex = sortStr.lastIndexOf('_');
            }

            sortType = sortTypeIndex > -1 && sortTypeIndex < sortStr.length() - 1 ?
                    sortStr.substring(sortTypeIndex + 1) : null;

            //排序规则的值可以为空字符串、空值、desc或asc
            Assert.isTrue(Strings.isNullOrEmpty(sortType) || sortType.equalsIgnoreCase("desc")
                    || sortType.equalsIgnoreCase("asc"), "请指定字段 " + sortStr + " 的排序规则。");

            if (sortType != null && sortType.equalsIgnoreCase("desc")) {
                newSort = new Sort(Sort.Direction.DESC, sortStr.substring(0, sortTypeIndex));
            } else {
                newSort = new Sort(Sort.Direction.ASC, sortType == null ? sortStr : sortStr.substring(0, sortTypeIndex));
            }

            if (sort == null) {
                sort = newSort;
            } else {
                //sort.and并没有修改sort对象
                sort = sort.and(newSort);
            }

        }
        return sort;
    }

    @Resource
    private D baseEntityDao;

    @Override
    public T getById(ID id) {
        Optional<T> result = baseEntityDao.findById(id);
        return result.isPresent() ? result.get() : null;
    }


    @Transactional
    @Override
    public void update(T entity, Collection<String> updateFields) {
        Set<String> newUpdateFields = new HashSet<String>();
        if (!CollectionUtils.isEmpty(updateFields)) {
            newUpdateFields.addAll(updateFields);
        }
        preUpdateHandler(entity, newUpdateFields);
        baseEntityDao.update(entity, newUpdateFields);
        postUpdateHandler(entity, newUpdateFields);
    }

    private Set<String> entityFields;


    /**
     * 更新之后的处理函数
     *
     * @param entity
     * @param updateFields
     */
    public void postUpdateHandler(T entity, Collection<String> updateFields) {

    }

    @Transactional
    @Override
    public void update(List<T> entityList, Collection<String> updateFields) {
        for (T entity : entityList) {
            update(entity, updateFields);
        }
    }

    /**
     * id是否为空
     */
    private boolean isBlankId(ID id) {
        return id == null || (id instanceof String) && Strings.isNullOrEmpty(id.toString());
    }


    @Override
    public boolean existsById(ID id) {
        return baseEntityDao.existsById(id);
    }

    @Override
    public Page<T> queryByPage(Map<String, Object> searchParams, int pageNum, int pageSize) {
        return queryByPage(searchParams, pageNum, pageSize, null);
    }


    @Override
    public List<T> find(Map<String, Object> searchParams) {
        return find(searchParams, null);
    }

    /**
     * 获取查询条件的表达式，用于匹配查询参数对应的查询条件，保存查询字段和数据库查询表达式的映射<br>
     *
     * @return
     */
    public abstract Map<String, String> getQueryExpressions();

    /**
     * 在执行查询时是否允许查询所有的记录，默认为false
     *
     * @return
     */
    public boolean isEnableQueryAllRecord() {
        return false;
    }

    /**
     * 获取格式化的查询条件表达式
     *
     * @param searchParams 由request中获取的查询参数
     */
    public LinkedHashMap<String, Object> formatQueryExpression(Map<String, Object> searchParams) {
        if (!isEnableQueryAllRecord()) {
            Assert.isTrue(MapUtils.isNotEmpty(searchParams), "查询参数Map不能为空。");
            Assert.isTrue(MapUtils.isNotEmpty(getQueryExpressions()), "查询条件的映射Map不能为空。");
        }
        LinkedHashMap<String, Object> queryMap = null;
        //匹配参数和service中提供的查询条件
        if (searchParams != null && searchParams.size() > 0) {
            for (Map.Entry<String, String> expressionMap : getQueryExpressions().entrySet()) {
                if (!searchParams.containsKey(expressionMap.getKey())) {
                    continue;
                }
                if (queryMap == null) {
                    queryMap = new LinkedHashMap<String, Object>();
                }
                queryMap.put(expressionMap.getValue(), searchParams.get(expressionMap.getKey()));
            }
        }
        log.debug("查询条件：" + JSON.toJSONString(queryMap, true));
        if (!isEnableQueryAllRecord()) {
            //为了查询数据安全，必须由已有的查询配置中获取查询条件
            Assert.isTrue(MapUtils.isNotEmpty(queryMap), "没有匹配的查询条件，查询参数必须由已有的查询表达式中获取。");
        }
        return queryMap;
    }
}
