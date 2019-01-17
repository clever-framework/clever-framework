package io.github.toquery.framework.dao.repository.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.dao.support.AppDaoEnumConnector;
import io.github.toquery.framework.dao.util.UtilJPA;
import io.github.toquery.framework.dao.validate.ValidateHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.CriteriaDeleteImpl;
import org.hibernate.query.criteria.internal.CriteriaUpdateImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.validation.ValidationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>扩展JpaRepository，添加对update的支持。</p>
 * <p>会根据查找到的接口名称，自动寻找后缀为Impl的接口实现类，</p>
 * <p>如果接口实现类需要交由spring管理，必须提供不带参数的构造方法。</p>
 */
@Slf4j
@Repository
public class AppJpaBaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements AppJpaBaseRepository<T, ID> {

    @Getter
    private final EntityManager entityManager;

    private final JpaEntityInformation<T, ?> entityInformation;

    /**
     * 是否允许全局的表单验证器，默认允许
     */
    private boolean enableValidator = true;

    public AppJpaBaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        this(domainClass, entityManager, false);
    }

    public AppJpaBaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager, boolean enableValidator) {
        //modified for spring data starter 1.3
        super(domainClass, entityManager);
        this.entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
        this.entityManager = entityManager;
        this.enableValidator = enableValidator;
        log.info("扩展JPA开始实例化{}", getClass().getSimpleName());
    }

    public AppJpaBaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        this(entityInformation, entityManager, false);
    }

    public AppJpaBaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager, boolean enableValidator) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
        this.enableValidator = enableValidator;
        log.info("扩展JPA开始实例化{}", getClass().getSimpleName());
        log.info("是否启用实体属性验证{}", this.enableValidator);
    }

    /**
     * 执行数量查询
     */
    protected Long executeCountQuery(TypedQuery<Long> query) {
        List<Long> totals = query.getResultList();
        Long total = 0L;

        for (Long element : totals) {
            total += element == null ? 0 : element;
        }

        return total;
    }

    @Override
    protected <S extends T> Page<S> readPage(TypedQuery<S> query, Class<S> domainClass, Pageable pageable, @Nullable Specification<S> spec) {
        super.readPage(query, domainClass, pageable, spec);

        //首先查询满足条件的记录数
        Long total = executeCountQuery(getCountQuery(spec, domainClass));

        //检查查询的数据是否超出总记录数，如果超出总记录数，默认查询最后一页，并修改pageable参数
        if (pageable.getOffset() >= total) {
            int pagenum = 0;
            //由于页号是从0开始，所以需要考虑整除的情况
            if (total % pageable.getPageSize() == 0) {
                pagenum = Ints.checkedCast(Math.max(total / pageable.getPageSize() - 1, 0));
            } else {
                pagenum = Ints.checkedCast(total / pageable.getPageSize());
            }
            log.info("总记录数为 {} ，当前页号为 {} 。超出分页查询范围，只返回最后一页（第{}页）的数据。", total, pageable.getPageNumber(), pagenum);
            pageable = PageRequest.of(pagenum, pageable.getPageSize());
        }
        //设置分页查询参数
        query.setFirstResult(Long.valueOf(pageable.getOffset()).intValue());
        query.setMaxResults(pageable.getPageSize());
        List<S> content = total > pageable.getOffset() ? query.getResultList() : Collections.<S>emptyList();
        return new PageImpl<S>(content, pageable, total);
    }

    /**
     * 验证实体，验证出现异常则抛出
     */
    public <S extends T> void validateEntity(S entity) {
        //进行实体验证
        if (enableValidator) {
            List<String> invalidmsg = ValidateHelper.validate(entity);
            if (CollectionUtils.isNotEmpty(invalidmsg)) {
                throw new ValidationException(Joiner.on(";").join(invalidmsg));
            }
        }
    }

    /**
     * 验证实体属性，验证出现异常则抛出
     */
    public <S extends T> void validateProperties(S entity, Collection<String> properties) {
        //进行实体验证
        if (enableValidator) {
            List<String> invalidmsg = ValidateHelper.validateProperties(entity, properties);
            if (CollectionUtils.isNotEmpty(invalidmsg)) {
                throw new ValidationException(Joiner.on(";").join(invalidmsg));
            }
        }
    }

    @Transactional
    public T update(T entity, Collection<String> updateFieldsName) {
        //检查更新实体是否具有id
        if (entityInformation.isNew(entity)) {
            throw new EntityNotFoundException("未找到将要修改对象 ，请检查主键信息 .");
        }
        //清理持久化上下文中的托管实体，避免重复更新
        entityManager.clear();
        //查询是否存在待更新的实体
        T existEntity = findExistEntity((ID) entityInformation.getId(entity));
        Assert.notNull(existEntity, "no exist " + entity.getClass() + " in database .");
        //如果存在需要更新的字段，则按照字段进行更新；否则整个更新实体
        if (isExecuteDynamicUpdate(entity, updateFieldsName)) {
            validateProperties(entity, updateFieldsName);
            //执行更新
            entity = updateSimpleField(entity, updateFieldsName);
        } else {
            //更新实体
            entity = updateComplexField(existEntity, entity, updateFieldsName);
        }
        return entity;
    }

    /**
     * 判断是否具有复杂的关联关系，如果具有则执行动态更新，否则执行完整更新
     */
    public boolean isExecuteDynamicUpdate(T entity, Collection<String> updateFieldsName) {
        //没有执行需要更新的字段
        if (updateFieldsName == null || updateFieldsName.size() < 1) {
            return false;
        }
        //获取数据库简单映射字段
        List<String> simpleFields = UtilJPA.getSimpleORMFieldInEntity(entity.getClass());
        //如果简单映射字段中不包括待更新的字段，返回false
        return simpleFields.containsAll(updateFieldsName);
    }

    /**
     * 使用动态更新的方法更新数据库简单映射字段的内容
     *
     * @param entity           要修改的对象
     * @param updateFieldsName 将要修改对象的字段
     */
    public T updateSimpleField(T entity, Collection<String> updateFieldsName) {
        Assert.notEmpty(updateFieldsName, entity.getClass() + "更新字段不能为空。");

        Assert.isTrue(!entityInformation.hasCompositeId(), "不支持组合ID更新。");
        CriteriaUpdate<T> criteriaUpdate = new CriteriaUpdateImpl<T>((CriteriaBuilderImpl) entityManager.getCriteriaBuilder());
        //更新的实体对象
        Root<T> root = criteriaUpdate.from(getDomainClass());
        for (String fieldName : updateFieldsName) {
            try {
                //通过反射读取属性的值
                criteriaUpdate.set(fieldName, PropertyUtils.getProperty(entity, fieldName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //构造更新过滤条件
        String idField = entityInformation.getIdAttributeNames().iterator().next();
        Predicate predicate = entityManager.getCriteriaBuilder().equal(root.get(idField), entityInformation.getId(entity));
        criteriaUpdate.where(predicate);
        //执行更新
        entityManager.createQuery(criteriaUpdate).executeUpdate();
        return entity;
    }

    /**
     * 更新复杂实体及相关属性
     *
     * @param existEntity      托管实体对象
     * @param newEntity        更新实体
     * @param updateFieldsName 更新字段
     */
    public T updateComplexField(T existEntity, T newEntity, Collection<String> updateFieldsName) {
        //设置托管实体中需要更新的属性
        if (updateFieldsName != null && updateFieldsName.size() > 0) {
            for (String updateField : updateFieldsName) {
                try {
                    //设置已有实体的属性
                    PropertyUtils.setProperty(existEntity, updateField, PropertyUtils.getProperty(newEntity, updateField));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e.getMessage());
                }
            }
        } else {
            throw new IllegalArgumentException("必须指定更新的字段");
        }
        //更新实体及相关属性
        newEntity = entityManager.merge(existEntity);

        return newEntity;
    }

    @Override
    @Transactional
    public List<T> update(List<T> entityList, Collection<String> updateFieldsName) {
        for (T entity : entityList) {
            entity = update(entity, updateFieldsName);
        }
        return entityList;
    }

    /**
     * 根据id查询已经存在的实体对象
     */
    private T findExistEntity(ID id) {
        //查询是否存在待更新的实体
        T existEntity = getOne(id);

        if (existEntity == null) {
            throw new IllegalArgumentException("can not find exist entity with primary : " + id.toString());
        }

        return existEntity;
    }

    @Override
    public Class<T> getDomainClass() {
        return entityInformation.getJavaType();
    }

    @Override
    public void deleteByIds(Collection<ID> ids) {
        ids.forEach(this::deleteById);
    }

    class CriteriaDeleteImpl2<T> extends CriteriaDeleteImpl<T> {

        protected CriteriaDeleteImpl2(CriteriaBuilderImpl criteriaBuilder) {
            super(criteriaBuilder);
        }
    }

    public void delete(Map<String, Object> params, AppDaoEnumConnector connector) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaDelete<T> criteriaDelete = new CriteriaDeleteImpl2<>((CriteriaBuilderImpl) builder);
        Root<T> root = criteriaDelete.from(getDomainClass());

        List<Predicate> predicateList = Lists.newArrayList();

        params.forEach((k, v) -> predicateList.add(builder.equal(root.get(k), v)));

        Predicate[] predicates = new Predicate[predicateList.size()];
        predicates = predicateList.toArray(predicates);

        Predicate predicate = null;
        if (connector == AppDaoEnumConnector.AND) {
             predicate = builder.and(predicates);
        }else if (connector == AppDaoEnumConnector.OR){
             predicate = builder.or(predicates);
        }
        criteriaDelete.where(predicate);
        entityManager.createQuery(criteriaDelete).executeUpdate();
    }

    protected <S> Root<T> applySpecificationToCriteria(Specification<T> spec, CriteriaQuery<S> query) {
        Root<T> root = query.from(getDomainClass());

        if (spec == null) {
            return root;
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        Predicate predicate = spec.toPredicate(root, query, builder);

        if (predicate != null) {
            query.where(predicate);
        }

        return root;
    }

    @Override
    public <S extends Number> S sum(String fieldName, Class<S> resultType, Specification<T> spec) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<S> query = builder.createQuery(resultType);

        //构建from和select
        Root<T> root = applySpecificationToCriteria(spec, query);

        query.select(builder.sum(root.get(fieldName).as(resultType)));

        TypedQuery<S> typedQuery = entityManager.createQuery(query);

        //进行查询
        return typedQuery.getSingleResult();
    }

    /**
     * 查询单个字段
     *
     * @param field 字段名称
     * @param spec  查询条件
     */
    public List<String> querySingleFields(String field, Specification<T> spec) {
        if (StringUtils.isBlank(field)) {
            return null;
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Object[]> query = criteriaBuilder.createQuery(Object[].class);

        Root<T> root = applySpecificationToCriteria(spec, query);

        //创建对字段的选择
        List<Selection<?>> selectionItems = new ArrayList<>(1);
        selectionItems.add(root.get(field));

        CompoundSelection<Object[]> compoundSelection = null;

        if (criteriaBuilder instanceof CriteriaBuilderImpl) {
            compoundSelection = ((CriteriaBuilderImpl) criteriaBuilder).array(selectionItems);
        }

        query.select(compoundSelection);

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);

        List<Object[]> list = typedQuery.getResultList();

        //转换查询结果
        List<String> resultList = null;

        if (list != null && list.size() > 0) {
            resultList = new ArrayList<>(list.size());
            //构造查询结果
            for (Object object : list) {
                if (object != null) {
                    resultList.add(object.toString());
                }
            }
        }
        return resultList;
    }

    public List<Map<String, Object>> queryMultiFields(String[] fields, Specification<T> spec) {
        if (fields == null || fields.length < 1) {
            return null;
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = criteriaBuilder.createQuery(Object[].class);
        Root<T> root = applySpecificationToCriteria(spec, query);
        //创建对字段的选择
        List<Selection<?>> selectionItems = new ArrayList<>(fields.length + 1);
        for (String field : fields) {
            selectionItems.add(root.get(field));
        }
        CompoundSelection<Object[]> compoundSelection = null;
        if (criteriaBuilder instanceof CriteriaBuilderImpl) {
            compoundSelection = ((CriteriaBuilderImpl) criteriaBuilder).array(selectionItems);
        }
        query.select(compoundSelection);
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
        List<Object[]> list = typedQuery.getResultList();
        //将Object[]转换为map
        List<Map<String, Object>> resultMap = new ArrayList<>(list.size());
        //如果只查询一个字段，查询结果不是一个数组，而只是一个对象
        if (fields.length == 1) {
            for (Object object : list) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put(fields[0], object);
                resultMap.add(itemMap);
            }
        } else {
            for (Object[] objects : list) {
                Map<String, Object> itemMap = new HashMap<>();
                for (int i = 0; i < fields.length; i++) {
                    itemMap.put(fields[i], objects[i]);
                }
                resultMap.add(itemMap);
            }
        }
        return resultMap;
    }

}
