package io.github.toquery.framework.mybatis.support;

import java.io.Serializable;

/**
 * mybatis基本操作类，包括根据id添加、查询、更新和删除。
 * @param <T> 实体类
 * @param <P> 实体类id属性的类型
 */
@MybatisRepository
public interface MybatisBaseRepository<T, P extends Serializable>{

}
