package io.github.toquery.framework.crud.service;

import org.springframework.data.domain.Page;

import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AppBaseService<E> {

    /**
     * 保存实体对象，如果是一个已经存在的实体，则进行更新。<br>
     * 如果是新创建的实体，属性中存在关联关系，需要首先清除保存实体关联关系，保存实体。<br>
     * 然后再重新设置关联关系，重新保存实体。
     */
    E save(E entity);

    /**
     * 保存实体对象，如果是一个已经存在的实体，则进行更新。<br>
     * 如果是新创建的实体，属性中存在关联关系，需要首先清除保存实体关联关系，保存实体。<br>
     * 然后再重新设置关联关系，重新保存实体。
     */
    List<E> save(List<E> entityIterable);

    /**
     * 根据id删除
     */
    void deleteById(Long id);

    /**
     * 根据ids批量删除
     */
    void deleteByIds(Iterable<Long> ids);

    void delete(Collection<E> deleteList);

    /**
     * 根据相应的条件参数删除数据，如果实现软删除接口则软删除
     */
    void delete(Map<String, Object> params);

    /**
     * 根据相应的条件参数删除数据，如果实现软删除接口则软删除
     */
    void delete(Map<String, Object> params, Predicate.BooleanOperator connector);

    /**
     * 根据ID判断实体是否存在
     */
    boolean existsById(Long id);

    /**
     * 判断满足条件的实体对象是否存在
     *
     * @param searchParams 查询条件
     */
    boolean exists(Map<String, Object> searchParams);

    /**
     * 根据id查询实体对象
     */
    E getById(Long id);


    /**
     * 更新实体对象指定字段内容<br>
     * 更新机制为：根据更新字段设置更新字段的内容，但是生成update语句和更新全部字段的SQL语句相同
     *
     * @param entity
     * @param updateFields 更新字段名称
     */
    E update(E entity, Collection<String> updateFields);

    /**
     * 批量更新实体对象内容<br>
     * 更新机制为：根据更新字段设置更新字段的内容，但是生成update语句和更新全部字段的SQL语句相同
     *
     * @param entityList
     * @param updateFields 更新字段名称
     */
    List<E> update(List<E> entityList, Collection<String> updateFields);


    /**
     * 查询满足条件的记录数
     *
     * @param searchParams
     * @return
     */
    long count(Map<String, Object> searchParams);

    /**
     * 不带排序的分页查询
     *
     * @param current  分页号，由0开始
     * @param pageSize 每页数据的大小
     */
    Page<E> queryByPage(int current, int pageSize);

    /**
     * 带排序的分页查询
     *
     * @param current  分页号，由0开始
     * @param pageSize 每页数据的大小
     * @param sorts        排序条件
     */
    Page<E> queryByPage(int current, int pageSize, String[] sorts);

    /**
     * 不带排序的分页查询
     *
     * @param searchParams 查询条件
     * @param current      分页号，由0开始
     * @param pageSize     每页数据的大小
     */
    Page<E> queryByPage(Map<String, Object> searchParams, int current, int pageSize);

    /**
     * 带排序的分页查询<br>
     * sorts中每个元素结构为："字段名称"或"字段名称_asc"或"字段名称_desc"，如果不带排序方式则默认为升序
     *
     * @param searchParams 查询条件
     * @param current      分页号，由0开始
     * @param pageSize     每页数据的大小
     * @param sorts        排序条件
     */
    Page<E> queryByPage(Map<String, Object> searchParams, int current, int pageSize, String[] sorts);



    /**
     * 查询所有实体
     */
    List<E> find();

    /**
     * 查询所有实体
     */
    List<E> find(String[] sorts);

    /**
     * 查询满足条件的所有实体。在分布式服务中慎用此方法，查询效率比分页查询效率要低。
     *
     * @param searchParams 查询条件
     */
    List<E> find(Map<String, Object> searchParams);

    /**
     * 查询满足条件的所有实体。在分布式服务中慎用此方法，查询效率比分页查询效率要低。
     *
     * @param searchParams 查询条件
     * @param sorts        排序条件
     */
    List<E> find(Map<String, Object> searchParams, String[] sorts);

    /**
     * 查询所有实体
     */
    List<E> findByIds(Collection<Long> ids);
}
