package io.github.toquery.framework.crud.service.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.core.entity.AppBaseEntity;
import io.github.toquery.framework.core.entity.AppEntityLogicDel;
import io.github.toquery.framework.jpa.jpa.support.DynamicJPASpecifications;
import io.github.toquery.framework.jpa.repository.AppJpaBaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.Transient;
import javax.persistence.criteria.Predicate;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * jpa快速curd方法
 *
 * @param <E> 实体类型
 * @param <D> Dao操作类
 */
@Slf4j
public abstract class AppBaseServiceImpl<E extends AppBaseEntity, D extends AppJpaBaseRepository<E>> implements AppBaseService<E> {

    @Autowired
    protected D repository;


    private Set<String> entityFields;

    /**
     * 保存之前的处理操作
     */
    protected void preSave(E entity) {

    }

    @Override
    @Transactional
    public E save(E entity) {
        preSave(entity);
        E e = this.repository.save(entity);
        postSave(entity);
        return e;
    }

    /**
     * 保存之后的处理操作
     */
    protected void postSave(E entity) {

    }

    /**
     * 保存之前的预处理操作
     */
    protected void preSaveBatch(List<E> entityList) {

    }

    @Override
    @Transactional
    public List<E> save(List<E> entityList) {
        preSaveBatch(entityList);
        List<E> saveData = this.repository.saveAllAndFlush(entityList);
        postSaveBatch(entityList);
        return saveData;
    }

    /**
     * 保存之后的处理操作
     */
    protected void postSaveBatch(List<E> entityList) {

    }

    public void delete(Collection<E> deleteList) {
        this.repository.deleteAll(deleteList);
    }

    /**
     * 根据id删除记录，删除时和当前对象的关联关系会被同步删除
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        if (id == null) {
            return;
        }
        if (isLogicDel()) {
            E entity = getById(id);
            if (entity != null) {
                //设置软删除
                ((AppEntityLogicDel) entity).setDeleted(true);
                this.update(entity, Lists.newArrayList("deleted"));
            }
        } else {
            this.repository.deleteById(id);
        }
        postDelete(id);
    }

    public void postDelete(Long id) {
        //在进行刷新操作前，先刷新当前实体缓存
        this.repository.flush();
    }

    /**
     * 批量删除记录，删除时和当前对象的关联关系会被同步删除
     */
    @Override
    @Transactional
    public void deleteByIds(Iterable<Long> ids) {
        for (Long id : ids) {
            if (id == null) {
                continue;
            }
            deleteById(id);
        }
    }

    @Override
    @Transactional
    public void delete(Map<String, Object> params, Predicate.BooleanOperator connector) {
        if (isLogicDel()) {
            List<E> entityList = this.list(params);
            entityList.forEach(entity -> {
                ((AppEntityLogicDel) entity).setDeleted(true);
            });
            this.update(entityList, Sets.newHashSet("deleted"));
        } else {
            this.repository.delete(params, connector);
        }
    }

    @Override
    @Transactional
    public void delete(Map<String, Object> params) {
        this.delete(params, Predicate.BooleanOperator.AND);
    }


    /**
     * 更新之前的预处理操作
     */
    public void preUpdate(E entity, Collection<String> updateFields) {
        Assert.notEmpty(updateFields, "必须指定更新的字段");

        //设置记录的更新时间
        entity.setUpdateDateTime(LocalDateTime.now());
        updateFields.add("updateDatetime");

        //todo 设置记录更新人的id
            /*AppAuthPrincipal appAuthPrincipal = appAuthPrincipalService.getAppAuthPrincipal();
            if (appAuthPrincipal != null && appAuthPrincipal.getAuthUser() != null && appAuthPrincipal.getAuthUser().getId() != null) {
                baseEntity.setLastUpdateUserId(appAuthPrincipal.getAuthUser().getId().toString());
                updateFields.add("lastUpdateUserId");
            }*/


        //获取当前实体的所有属性
        if (entityFields == null || entityFields.size() <= 0) {
            entityFields = FieldUtils.getAllFieldsList(entity.getClass()).stream().filter(field -> field.getAnnotation(Transient.class) == null).map(Field::getName).collect(Collectors.toSet());
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
     * 是否是逻辑删除。如果是软删除需要在响应的底层服务中进行相关逻辑处理
     *
     * @return
     */
    public boolean isLogicDel() {
        boolean isSoftDel = ClassUtils.isAssignable(this.repository.getDomainClass(), AppEntityLogicDel.class);
        if (isSoftDel) {
            log.info("{} 删除为软删除", this.repository.getDomainClass().getName());
        }
        return isSoftDel;
    }

    @Override
    public long count(Map<String, Object> searchParams) {
        return this.count(this.getQueryExpressions(), searchParams);
    }


    @Override
    public long count(Map<String, String> queryExpressions, Map<String, Object> searchParams) {
        LinkedHashMap<String, Object> queryExpressionMap = formatQueryExpression(queryExpressions, searchParams);
        Specification<E> specification = DynamicJPASpecifications.bySearchParam(queryExpressionMap, this.repository.getDomainClass());
        return this.repository.count(specification);
    }

    /**
     * 不带排序的分页查询
     *
     * @param current  分页号，由0开始
     * @param pageSize 每页数据的大小
     */
    public Page<E> page(int current, int pageSize) {
        return this.page(current, pageSize, null);
    }

    /**
     * 带排序的分页查询
     *
     * @param current  分页号，由0开始
     * @param pageSize 每页数据的大小
     * @param sorts    排序条件
     */
    public Page<E> page(int current, int pageSize, String[] sorts) {
        Pageable pageable = PageRequest.of(current, pageSize, getSort(sorts));
        return this.repository.findAll(pageable);
    }

    @Override
    public Page<E> page(Map<String, Object> searchParams, int current, int pageSize, String[] sorts) {
        return this.page(this.getQueryExpressions(), searchParams, current, pageSize, sorts);
    }


    @Override
    public Page<E> page(Map<String, String> queryExpressions, Map<String, Object> searchParams, int current, int pageSize, String[] sorts) {
        log.info("获取的原始查询参数->" + JacksonUtils.object2String(searchParams));

        Specification<E> specification = getQuerySpecification(queryExpressions, searchParams);

        Pageable pageable = PageRequest.of(current, pageSize, getSort(sorts));

        return this.repository.findAll(specification, pageable);

    }


    /**
     * 获取查询条件的Specification对象
     *
     * @param searchParams 查询条件map
     * @return
     */
    public Specification<E> getQuerySpecification(Map<String, String> queryExpressions, Map<String, Object> searchParams) {
        LinkedHashMap<String, Object> queryExpressionMap = formatQueryExpression(queryExpressions, searchParams);

        //如果是软删除，默认查询未删除的记录
        if (isLogicDel()) {
            if (queryExpressionMap == null) {
                queryExpressionMap = Maps.newLinkedHashMap();
            }
            if (!queryExpressionMap.containsKey("deleted:BOOLEANQE")) {
                queryExpressionMap.put("deleted:BOOLEANQE", false);
                log.info("添加软删除的查询参数 deleted:BOOLEANQE，查询没有删除的记录。");
            }
        }

        //构建查询条件
        return DynamicJPASpecifications.bySearchParam(queryExpressionMap, this.repository.getDomainClass());
    }

    /**
     * 获取排序类型，sorts中元素结构为："字段名称"或"字段名称_asc"或"字段名称_desc"，如果不带排序方式则默认为升序。<br>
     * 其中下划线也可以改为“:”
     */
    protected Sort getSort(String[] sorts) {
        //默认按照创建时间排序
        if ((sorts == null || sorts.length < 1) && AppBaseEntity.class.isAssignableFrom(this.repository.getDomainClass())) {
            sorts = new String[]{"createDateTime"};
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

            sortType = sortTypeIndex > -1 && sortTypeIndex < sortStr.length() - 1 ? sortStr.substring(sortTypeIndex + 1) : null;

            //排序规则的值可以为空字符串、空值、desc或asc
            Assert.isTrue(Strings.isNullOrEmpty(sortType) || sortType.equalsIgnoreCase("desc") || sortType.equalsIgnoreCase("asc"), "请指定字段 " + sortStr + " 的排序规则。");

            if (sortType != null && sortType.equalsIgnoreCase("desc")) {
                newSort = Sort.by(Sort.Direction.DESC, sortStr.substring(0, sortTypeIndex));
            } else {
                newSort = Sort.by(Sort.Direction.ASC, sortType == null ? sortStr : sortStr.substring(0, sortTypeIndex));
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


    @Override
    public E getById(Long id) {
        Optional<E> result = this.repository.findById(id);
        return result.orElse(null);
    }

    @Override
    public E update(E entity) {
        return this.repository.saveAndFlush(entity);
    }

    @Transactional
    @Override
    public E update(E entity, Collection<String> updateFields) {
        Set<String> newUpdateFields = new HashSet<String>();
        if (updateFields != null && !updateFields.isEmpty()) {
            newUpdateFields.addAll(updateFields);
        }
        preUpdate(entity, newUpdateFields);
        E e = this.repository.update(entity, newUpdateFields);
        postUpdate(entity, newUpdateFields);
        return e;
    }



    /**
     * 更新之后的处理函数
     *
     * @param entity
     * @param updateFields
     */
    public void postUpdate(E entity, Collection<String> updateFields) {

    }

    @Transactional
    public List<E> update(List<E> entityList) {
        List<E> list = Lists.newArrayList();
        for (E entity : entityList) {
            E e = this.update(entity);
            list.add(e);
        }
        return list;
    }

    @Transactional
    @Override
    public List<E> update(List<E> entityList, Collection<String> updateFields) {
        List<E> list = Lists.newArrayList();
        for (E entity : entityList) {
            E e = update(entity, updateFields);
            list.add(e);
        }
        return list;
    }


    @Override
    public boolean existsById(Long id) {
        return this.repository.existsById(id);
    }

    @Override
    public boolean exists(Map<String, Object> searchParams) {
        return this.exists(this.getQueryExpressions(), searchParams);
    }

    @Override
    public boolean exists(Map<String, String> queryExpressions, Map<String, Object> searchParams) {
        LinkedHashMap<String, Object> queryExpressionMap = formatQueryExpression(queryExpressions, searchParams);
        Specification<E> specification = DynamicJPASpecifications.bySearchParam(queryExpressionMap, this.repository.getDomainClass());
        return this.repository.count(specification) > 0;
    }

    @Override
    public Page<E> page(Map<String, Object> searchParams, int current, int pageSize) {
        return this.page(searchParams, current, pageSize, null);
    }


    @Override
    public List<E> list() {
        return this.repository.findAll();
    }

    /**
     * 查询所有实体
     */
    public List<E> list(String[] sorts) {
        return this.repository.findAll(this.getSort(sorts));
    }

    @Override
    public List<E> list(Map<String, Object> searchParams) {
        return list(searchParams, null);
    }

    public List<E> list(Map<String, Object> searchParams, String[] sorts) {
        return this.list(this.getQueryExpressions(), searchParams, sorts);
    }

    @Override
    public List<E> list(Map<String, String> queryExpressions, Map<String, Object> searchParams, String[] sorts) {
        Specification<E> specification = getQuerySpecification(queryExpressions, searchParams);
        return this.repository.findAll(specification, getSort(sorts));
    }

    @Override
    public List<E> listByIds(Collection<Long> ids) {
        if (ids == null || ids.size() <= 0) {
            return Lists.newArrayList();
        }
        return this.repository.findAllById(ids);
    }

    /**
     * 获取查询条件的表达式，用于匹配查询参数对应的查询条件，保存查询字段和数据库查询表达式的映射<br>
     *
     * @return  查询条件的表达式
     */
    public abstract Map<String, String> getQueryExpressions();

    /**
     * 在执行查询时是否允许查询所有的记录，默认为 true
     *
     * @return 是否查询所有记录
     */
    public boolean isEnableQueryAllRecord() {
        return true;
    }

    /**
     * 获取格式化的查询条件表达式
     *
     * @param searchParams 由request中获取的查询参数
     */
    public LinkedHashMap<String, Object> formatQueryExpression(Map<String, String> queryExpressions, Map<String, Object> searchParams) {
        if (!isEnableQueryAllRecord()) {
            Assert.isTrue(searchParams == null || searchParams.size() <= 0, "查询参数Map不能为空。");
            Assert.isTrue(queryExpressions == null || queryExpressions.size() <= 0, "查询条件的映射Map不能为空。");
        }
        LinkedHashMap<String, Object> queryMap = null;
        //匹配参数和service中提供的查询条件
        if (searchParams != null && searchParams.size() > 0) {
            for (Map.Entry<String, String> expressionMap : queryExpressions.entrySet()) {
                if (!searchParams.containsKey(expressionMap.getKey())) {
                    continue;
                }
                if (queryMap == null) {
                    queryMap = new LinkedHashMap<String, Object>();
                }
                queryMap.put(expressionMap.getValue(), searchParams.get(expressionMap.getKey()));
            }
        }
        log.debug("查询条件：" + JacksonUtils.object2String(queryMap));
        if (!isEnableQueryAllRecord()) {
            //为了查询数据安全，必须由已有的查询配置中获取查询条件
            Assert.isTrue(queryMap == null || queryMap.size() <= 0, "没有匹配的查询条件，查询参数必须由已有的查询表达式中获取。");
        }
        return queryMap;
    }
}
