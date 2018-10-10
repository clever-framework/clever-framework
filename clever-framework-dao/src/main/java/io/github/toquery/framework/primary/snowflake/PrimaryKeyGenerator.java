package io.github.toquery.framework.primary.snowflake;

/**
 * id生成器
 * @param <T>
 */
public interface PrimaryKeyGenerator<T> {

    /**
     * 生成id
     */
    T getNextId() ;

}
