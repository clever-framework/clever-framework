package io.github.toquery.framework.dao.primary.snowflake;

/**
 * 带有上下文的id生成
 *
 * @param <T> 生成的主键类型
 */
public abstract class ContextPrimaryKeyGenerator<T> implements PrimaryKeyGenerator<T> {
    @Override
    public T getNextId() {
        return getNextId(null);
    }

    /**
     * 根据上下文生成id
     *
     * @param object 原始键
     * @return 主键
     */
    public abstract T getNextId(Object object);
}
