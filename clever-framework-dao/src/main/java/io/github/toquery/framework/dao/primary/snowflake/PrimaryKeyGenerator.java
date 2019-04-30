package io.github.toquery.framework.dao.primary.snowflake;

/**
 * id生成器
 *
 * @param <T> 生成的主键类型
 */
public interface PrimaryKeyGenerator<T> {

    /**
     * 生成id
     * @return 生成的主键
     */
    T getNextId();

}
