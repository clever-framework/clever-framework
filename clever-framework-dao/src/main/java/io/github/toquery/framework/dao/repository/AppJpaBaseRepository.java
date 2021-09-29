package io.github.toquery.framework.dao.repository;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@NoRepositoryBean
public interface AppJpaBaseRepository<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    /**
     * 统计加和运算
     *
     * @param spec       查询条件
     * @param fieldName  字段名称
     * @param resultType 返回值类型
     * @return
     */
    <S extends Number> S sum(String fieldName, Class<S> resultType, Specification<T> spec);

    /**
     * 查询多个字段
     *
     * @param fields 查询字段数组
     * @param spec   查询条件
     * @return 返回list，每个元素为一个map对象，存储字段名称和字段值的映射
     */
    List<Map<String, Object>> queryMultiFields(String[] fields, Specification<T> spec);

    /**
     * 查询单个字段
     *
     * @param field 字段名称
     * @param spec  查询条件
     * @return 返回待查询字段的list对象
     */
    List<String> querySingleFields(String field, Specification<T> spec);

    /**
     * 获取当前dao对应的实体管理器
     *
     * @return 实体管理
     */
    EntityManager getEntityManager();

    /**
     * 更新实体对象
     *
     * @param entity           实体
     * @param updateFieldsName 更新字段名称
     */
    T update(T entity, Collection<String> updateFieldsName);

    /**
     * 批量更新实体对象
     *
     * @param entityList       实体集合
     * @param updateFieldsName 更新实体字段名称
     */
    List<T> update(List<T> entityList, Collection<String> updateFieldsName);

    /**
     * 获取进行操作的领域类
     */
    Class<T> getDomainClass();

    /**
     * 根据ID循环删除数据
     *
     */
    void deleteByIds(Collection<Long> ids);

    /**
     * 根据相应的条件参数删除数据
     */
    void delete(Map<String,Object> params, Predicate.BooleanOperator connector);
}
