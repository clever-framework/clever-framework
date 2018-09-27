package io.github.toquery.framework.id;

/**
 * 带有上下文的id生成
 * @param <T>
 */
public abstract class ContextIdGenerator<T> implements IdGenerator<T> {
    @Override
    public T getNextId() {
        return getNextId(null) ;
    }

    /**
     * 根据上下文生成id
     * @param object
     * @return
     */
    public abstract T getNextId( Object object ) ;
}
