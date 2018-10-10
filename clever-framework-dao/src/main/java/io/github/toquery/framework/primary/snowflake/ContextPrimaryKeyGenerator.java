package io.github.toquery.framework.primary.snowflake;

/**
 * 带有上下文的id生成
 * @param <T>
 */
public abstract class ContextPrimaryKeyGenerator<T> implements PrimaryKeyGenerator<T> {
    @Override
    public T getNextId() {
        return getNextId(null) ;
    }

    /**
     * 根据上下文生成id
     */
    public abstract T getNextId( Object object ) ;
}
