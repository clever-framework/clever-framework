package io.github.toquery.framework.crud.service.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.entity.AppEntitySoftDel;
import io.github.toquery.framework.dao.jpa.support.DynamicJPASpecifications;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
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
import java.util.stream.Collectors;

/**
 * jpa快速curd方法
 *
 * @param <ID> 主键类型
 * @param <E>  实体类型
 * @param <D>  Dao操作类
 */
@Slf4j
public abstract class AppBaseServiceImpl<ID extends Serializable, E extends AppBaseEntity, D extends AppJpaBaseRepository<E, ID>> implements AppBaseService<E, ID> {

    @Autowired
    protected D dao;

    @Override
    @Transactional
    public E save(E entity) {
        preSaveHandler(entity);
        E e = this.dao.save(entity);
        postSaveHandler(entity);
        return e;
    }

    /**
     * 保存之前的处理操作
     */
    protected void preSaveHandler(E entity) {

    }

    /**
     * 保存之后的处理操作
     */
    protected void postSaveHandler(E entity) {

    }

    @Override
    @Transactional
    public List<E> save(List<E> entityList) {
        preSaveBatchHandler(entityList);
        List<E> saveData = this.dao.saveAll(entityList);
        postSaveBatchHandler(entityList);
        return saveData;
    }

    /**
     * 保存之前的预处理操作
     */
    protected void preSaveBatchHandler(List<E> entityList) {

    }


    /**
     * 保存之后的处理操作
     */
    protected void postSaveBatchHandler(List<E> entityList) {

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
        if (isSoftDel()) {
            E entity = getById(id);
            if (entity != null) {
                //设置软删除
                ((AppEntitySoftDel) entity).setDeleted(true);
                this.update(entity, Lists.newArrayList("deleted"));
            }
        } else {
            this.dao.deleteById(id);
        }
        postDeleteHandler(id);
    }

    public void postDeleteHandler(ID id) {
        //在进行刷新操作前，先刷新当前实体缓存
        this.dao.flush();
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

    @Override
    @Transactional
    public void delete(Map<String, Object> params, Predicate.BooleanOperator connector) {
        if (isSoftDel()) {
            List<E> entityList = this.find(params);
            this.update(entityList, Sets.newHashSet(""));
        } else {
            this.dao.delete(params, connector);
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
    public void preUpdateHandler(E entity, Collection<String> updateFields) {
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
     * 是否是软删除。如果是软删除需要在响应的底层服务中进行相关逻辑处理
     *
     * @return
     */
    public boolean isSoftDel() {
        boolean isSoftDel = ClassUtils.isAssignable(this.dao.getDomainClass(), AppEntitySoftDel.class);
        if (isSoftDel) {
            log.info("{} 删除为软删除", this.dao.getDomainClass().getName());
        }
        return isSoftDel;
    }




    @Override
    public long count(Map<String, Object> searchParams) {
        LinkedHashMap<String, Object> queryExpressionMap = formatQueryExpression(searchParams);
        Specification<E> specification = DynamicJPASpecifications.bySearchParam(queryExpressionMap, this.dao.getDomainClass());
        return this.dao.count(specification);
    }

    /**
     * 不带排序的分页查询
     *
     * @param pageNum  分页号，由0开始
     * @param pageSize 每页数据的大小
     */
    public Page<E> queryByPage(int pageNum, int pageSize){
        return this.queryByPage(pageNum, pageSize, null);
    }

    /**
     * 带排序的分页查询
     *
     * @param pageNum  分页号，由0开始
     * @param pageSize 每页数据的大小
     * @param sorts        排序条件
     */
    public Page<E> queryByPage(int pageNum, int pageSize, String[] sorts){
        Pageable pageable = PageRequest.of(pageNum, pageSize, getSort(sorts));
        return this.dao.findAll(pageable);
    }

    @Override
    public Page<E> queryByPage(Map<String, Object> searchParams, int pageNum, int pageSize, String[] sorts) {
        log.info("获取的原始查询参数->" + JacksonUtils.object2String(searchParams));

        Specification<E> specification = getQuerySpecification(searchParams);

        Pageable pageable = PageRequest.of(pageNum, pageSize, getSort(sorts));

        return this.dao.findAll(specification, pageable);

//		Page<E> page = entityDao.findAll(specification, pageable) ;
//		//将page对象转换为可以在dubbo中进行序列化和反序列化的分页对象
//		//modified by dc , 根据分页查询结果修重新创建分页参数对象。
//		//因为传入的分页参数并不一定都要分页数据
//		return new PageImplInDubbo<E>(page.getContent() ,
//				new PageRequest(page.getNumber() , page.getSize()) , page.getTotalElements()) ;
    }


    /**
     * 获取查询条件的Specification对象
     *
     * @param searchParams 查询条件map
     * @return
     */
    public Specification<E> getQuerySpecification(Map<String, Object> searchParams) {
        LinkedHashMap<String, Object> queryExpressionMap = formatQueryExpression(searchParams);

        //如果是软删除，默认查询未删除的记录
        if (isSoftDel()) {
            if (queryExpressionMap == null) {
                queryExpressionMap = Maps.newLinkedHashMap();
            }
            if (!queryExpressionMap.containsKey("deleted:BOOLEANQE")) {
                queryExpressionMap.put("deleted:BOOLEANQE", false);
                log.info("添加软删除的查询参数 deleted:BOOLEANQE，查询没有删除的记录。");
            }
        }

        //构建查询条件
        return DynamicJPASpecifications.bySearchParam(queryExpressionMap, this.dao.getDomainClass());
    }

    /**
     * 获取排序类型，sorts中元素结构为："字段名称"或"字段名称_asc"或"字段名称_desc"，如果不带排序方式则默认为升序。<br>
     * 其中下划线也可以改为“:”
     */
    protected Sort getSort(String[] sorts) {
        //默认按照创建时间排序
        if ((sorts == null || sorts.length < 1) && AppBaseEntity.class.isAssignableFrom(this.dao.getDomainClass())) {
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
    public E getById(ID id) {
        Optional<E> result = this.dao.findById(id);
        return result.isPresent() ? result.get() : null;
    }


    @Transactional
    @Override
    public E update(E entity, Collection<String> updateFields) {
        Set<String> newUpdateFields = new HashSet<String>();
        if (!CollectionUtils.isEmpty(updateFields)) {
            newUpdateFields.addAll(updateFields);
        }
        preUpdateHandler(entity, newUpdateFields);
        E e = this.dao.update(entity, newUpdateFields);
        postUpdateHandler(entity, newUpdateFields);
        return e;
    }

    private Set<String> entityFields;


    /**
     * 更新之后的处理函数
     *
     * @param entity
     * @param updateFields
     */
    public void postUpdateHandler(E entity, Collection<String> updateFields) {

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

    /**
     * id是否为空
     */
    private boolean isBlankId(ID id) {
        return id == null || (id instanceof String) && Strings.isNullOrEmpty(id.toString());
    }


    @Override
    public boolean existsById(ID id) {
        return this.dao.existsById(id);
    }

    @Override
    public boolean exists(Map<String, Object> searchParams) {
        LinkedHashMap<String, Object> queryExpressionMap = formatQueryExpression(searchParams);
        Specification<E> specification = DynamicJPASpecifications.bySearchParam(queryExpressionMap, this.dao.getDomainClass());
        return this.dao.count(specification) > 0;
    }

    @Override
    public Page<E> queryByPage(Map<String, Object> searchParams, int pageNum, int pageSize) {
        return this.queryByPage(searchParams, pageNum, pageSize, null);
    }



    @Override
    public List<E> find() {
        return this.dao.findAll();
    }

    /**
     * 查询所有实体
     */
    public List<E> find(String[] sorts){
        return this.dao.findAll(this.getSort(sorts));
    }

    @Override
    public List<E> find(Map<String, Object> searchParams) {
        return find(searchParams, null);
    }


    @Override
    public List<E> find(Map<String, Object> searchParams, String[] sorts) {
        Specification<E> specification = getQuerySpecification(searchParams);
        return this.dao.findAll(specification, getSort(sorts));
    }

    @Override
    public List<E> findByIds(Collection<ID> ids) {
        if (ids == null || ids.size() <= 0){
            return Lists.newArrayList();
        }
        return this.dao.findAllById(ids);
    }

    /**
     * 获取查询条件的表达式，用于匹配查询参数对应的查询条件，保存查询字段和数据库查询表达式的映射<br>
     *
     * @return
     */
    public abstract Map<String, String> getQueryExpressions();

    /**
     * 在执行查询时是否允许查询所有的记录，默认为 true
     *
     * @return
     */
    public boolean isEnableQueryAllRecord() {
        return true;
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
        log.debug("查询条件：" + JacksonUtils.object2String(queryMap));
        if (!isEnableQueryAllRecord()) {
            //为了查询数据安全，必须由已有的查询配置中获取查询条件
            Assert.isTrue(MapUtils.isNotEmpty(queryMap), "没有匹配的查询条件，查询参数必须由已有的查询表达式中获取。");
        }
        return queryMap;
    }
}
