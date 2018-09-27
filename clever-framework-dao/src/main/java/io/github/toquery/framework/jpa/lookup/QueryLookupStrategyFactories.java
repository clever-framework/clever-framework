package io.github.toquery.framework.jpa.lookup;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;

import javax.persistence.EntityManager;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 方法查询策略。主要检测mybatis查询方法
 */
@Slf4j
public class QueryLookupStrategyFactories implements QueryLookupStrategy {

    private BeanFactory beanFactory ;

    private final EntityManager entityManager;

    private QueryLookupStrategy defaultQueryLookupStrategy ;

    private List<QueryLookupStrategyAdvice> queryLookupStrategyAdvices = Lists.newArrayList( );

    private QueryExtractor extractor ;

    public QueryLookupStrategyFactories(EntityManager entityManager, BeanFactory beanFactory  ,
                                        Key key, QueryExtractor extractor, EvaluationContextProvider evaluationContextProvider) {
        //默认方法查询策略使用jpa
        this.defaultQueryLookupStrategy = JpaQueryLookupStrategy.create(entityManager, key, extractor, evaluationContextProvider) ;
        this.extractor = extractor ;
        this.entityManager = entityManager;
        this.beanFactory = beanFactory ;

        //添加不同类型资源库的查询策略
        queryLookupStrategyAdvices.add( new MybatisQueryLookupStrategy( beanFactory ) ) ;
    }

    public static QueryLookupStrategy create(EntityManager entityManager, BeanFactory beanFactory  ,
                                             Key key, QueryExtractor extractor, EvaluationContextProvider evaluationContextProvider) {
        return new QueryLookupStrategyFactories(entityManager , beanFactory ,
                key , extractor , evaluationContextProvider);
    }

    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata,
                                        ProjectionFactory factory, NamedQueries namedQueries) {
        //查找资源库策略
        QueryLookupStrategy queryLookupStrategy = null ;
        for (QueryLookupStrategyAdvice queryLookupStrategyAdvice : queryLookupStrategyAdvices) {
            if( !queryLookupStrategyAdvice.isEnabled( method , metadata ) ){
                continue;
            }
            queryLookupStrategy = queryLookupStrategyAdvice ;

            log.info("使用{}的数据库持久化查询策略" , queryLookupStrategyAdvice.getName() );
            break;
        }

        if( queryLookupStrategy==null ){
            queryLookupStrategy = defaultQueryLookupStrategy ;
            log.info("使用{}的数据库持久化查询策略" , "jpa");
        }

        return queryLookupStrategy.resolveQuery(method, metadata, factory , namedQueries);
    }
}
