package io.github.toquery.framework.id;

/**
 * id生成器
 * @param <T>
 */
public interface IdGenerator<T> {

    /**
     * 生成id
     * @return
     */
    T getNextId() ;

}
