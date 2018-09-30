package io.github.toquery.framework.jpa.lookup;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;

import java.lang.reflect.Method;

public abstract class QueryLookupStrategyAdvice implements QueryLookupStrategy{

    protected BeanFactory beanFactory ;

    public QueryLookupStrategyAdvice(BeanFactory beanFactory){
        this.beanFactory = beanFactory ;
    }

    /**
     * 获取名称
     */
    abstract String getName() ;

    /**
     * 是否允许
     */
    abstract boolean isEnabled(Method method , RepositoryMetadata metadata ) ;

}
