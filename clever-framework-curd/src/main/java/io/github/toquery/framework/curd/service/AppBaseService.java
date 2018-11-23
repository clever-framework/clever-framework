package io.github.toquery.framework.curd.service;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface AppBaseService<T, ID extends Serializable> {

    /**
     * 保存实体对象，如果是一个已经存在的实体，则进行更新。<br>
     * 如果是新创建的实体，属性中存在关联关系，需要首先清除保存实体关联关系，保存实体。<br>
     * 然后再重新设置关联关系，重新保存实体。
     */
    T save(T entity);

    /**
     * 保存实体对象，如果是一个已经存在的实体，则进行更新。<br>
     * 如果是新创建的实体，属性中存在关联关系，需要首先清除保存实体关联关系，保存实体。<br>
     * 然后再重新设置关联关系，重新保存实体。
     */
    List<T> saveBatch(List<T> entityIterable);

    /**
     * 根据id删除
     */
    void deleteById(ID id);

    /**
     * 根据ids批量删除
     */
    void deleteByIds(Iterable<ID> ids);

    /**
     * 根据ID判断实体是否存在
     */
    boolean existsById(ID id);

    /**
     * 判断满足条件的实体对象是否存在
     *
     * @param searchParams 实体查询条件
     */
    boolean existsById(Map<String, Object> searchParams);

    /**
     * 根据id查询实体对象
     */
    T getById(ID id);


    /**
     * 更新实体对象指定字段内容<br>
     * 更新机制为：根据更新字段设置更新字段的内容，但是生成update语句和更新全部字段的SQL语句相同
     *
     * @param entity
     * @param updateFields 更新字段名称
     */
    T update(T entity, Collection<String> updateFields);

    /**
     * 批量更新实体对象内容<br>
     * 更新机制为：根据更新字段设置更新字段的内容，但是生成update语句和更新全部字段的SQL语句相同
     *
     * @param entityList
     * @param updateFields 更新字段名称
     */
    List<T> update(List<T> entityList, Collection<String> updateFields);


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
     * @param searchParams 查询条件
     * @param pageNum      分页号，由0开始
     * @param pageSize     每页数据的大小
     */
    Page<T> queryByPage(Map<String, Object> searchParams, int pageNum, int pageSize);

    /**
     * 带排序的分页查询<br>
     * sorts中每个元素结构为："字段名称"或"字段名称_asc"或"字段名称_desc"，如果不带排序方式则默认为升序
     *
     * @param searchParams 查询条件
     * @param pageNum      分页号，由0开始
     * @param pageSize     每页数据的大小
     * @param sorts        排序条件
     */
    Page<T> queryByPage(Map<String, Object> searchParams, int pageNum, int pageSize, String[] sorts);

    /**
     * 查询满足条件的所有实体。在分布式服务中慎用此方法，查询效率比分页查询效率要低。
     *
     * @param searchParams 查询条件
     * @param sorts        排序条件
     */
    List<T> find(Map<String, Object> searchParams, String[] sorts);

    /**
     * 查询满足条件的所有实体。在分布式服务中慎用此方法，查询效率比分页查询效率要低。
     *
     * @param searchParams 查询条件
     */
    List<T> find(Map<String, Object> searchParams);

}