package io.github.toquery.framework.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.toquery.framework.repository.AppBaseDataRepository;
import io.github.toquery.framework.service.AppBaseDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.reflections.ReflectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
public abstract class AppBaseDataServiceImpl<T, D extends AppBaseDataRepository<T, ID>, ID extends Serializable> implements AppBaseDataService<T, ID> {

    @Resource
    private D baseEntityDao;

    @Override
    public T get(ID id) {
        Optional<T> result = baseEntityDao.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    @Transactional
    @Override
    public T save(T entity) {
        preSaveHandler(entity);
        T t = baseEntityDao.save(entity);
        postSaveHandler(entity);
        return t;
    }

    /**
     * 保存之前的预处理操作
     */
    public void preSaveHandler(T entity) {

    }

    /**
     * 保存之后的处理操作
     *
     */
    public void postSaveHandler(T entity) {

    }

    @Transactional
    @Override
    public void saveBatch(Collection<T> entityCollection) {
        preSaveBatchHandler(entityCollection);
        entityCollection.forEach(this::save);
        postSaveBatchHandler(entityCollection);
    }

    /**
     * 保存之前的预处理操作
     */
    public void preSaveBatchHandler(Collection<T> entityCollection) {

    }

    /**
     * 保存之后的处理操作
     *
     * @param entityCollection
     */
    public void postSaveBatchHandler(Collection<T> entityCollection) {

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
     * 更新之前的预处理操作
     */
    public void preUpdateHandler(T entity, Collection<String> updateFields) {
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
     *
     * @param id
     * @return
     */
    protected boolean isBlankId(ID id) {
        return id == null || (id instanceof String) && Strings.isNullOrEmpty(id.toString());
    }

    /**
     * 批量删除记录，删除时和当前对象的关联关系会被同步删除
     */
    @Transactional
    @Override
    public void delete(ID[] ids) {
        this.delete(Arrays.asList(ids));
    }

    /**
     * 批量删除记录，删除时和当前对象的关联关系会被同步删除
     */
    @Transactional
    @Override
    public void delete(Iterable<ID> ids) {
        for (ID id : ids) {
            if (isBlankId(id)) {
                continue;
            }
            delete(id);
        }
    }

    /**
     * 根据id删除记录，删除时和当前对象的关联关系会被同步删除
     */
    @Transactional
    @Override
    public void delete(ID id) {
        if (isBlankId(id)) {
            return;
        }

        baseEntityDao.deleteById(id);

        postDeleteHandler(id);
    }

    public void postDeleteHandler(ID id) {

    }

    @Override
    public boolean isExist(ID id) {
        return baseEntityDao.existsById(id);
    }

    @Override
    public Page<T> queryByPage(Map<String, Object> searchParams, int pageNum, int pageSize) {
        return queryByPage(searchParams, pageNum, pageSize, null);
    }

    /**
     * 获取排序类型，sorts中元素结构为："字段名称"或"字段名称_asc"或"字段名称_desc"，如果不带排序方式则默认为升序。<br>
     * 其中下划线也可以改为“:”
     *
     * @param sorts
     * @return
     */
    protected Sort getSort(String[] sorts) {
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
                newSort = new Sort(Sort.Direction.ASC,
                        sortType == null ? sortStr : sortStr.substring(0, sortTypeIndex));
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
    public List<T> findAll(Map<String, Object> searchParams) {
        return findAll(searchParams, null);
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
     * @return
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
