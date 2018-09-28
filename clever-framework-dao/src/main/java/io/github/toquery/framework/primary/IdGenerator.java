package io.github.toquery.framework.primary;

/**
 * id生成器
 * @param <T>
 */
public interface IdGenerator<T> {

    /**
     * 生成id
     */
    T getNextId() ;

}
