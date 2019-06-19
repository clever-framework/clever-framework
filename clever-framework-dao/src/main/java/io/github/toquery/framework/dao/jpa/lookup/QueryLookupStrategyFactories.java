package io.github.toquery.framework.dao.jpa.lookup;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;

import javax.persistence.EntityManager;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * 方法查询策略。主要检测mybatis查询方法
 */
@Slf4j
public class QueryLookupStrategyFactories implements QueryLookupStrategy {

    private BeanFactory beanFactory;

    private final EntityManager entityManager;

    // 默认数据库查询策略
    private QueryLookupStrategy defaultQueryLookupStrategy;

    private List<QueryLookupStrategyAdvice> queryLookupStrategyAdvices = Lists.newArrayList();

    private QueryExtractor extractor;

    public QueryLookupStrategyFactories(EntityManager entityManager, BeanFactory beanFactory, Key key, QueryExtractor extractor, QueryMethodEvaluationContextProvider queryMethodEvaluationContextProvider) {
        //默认方法查询策略使用jpa
        this.defaultQueryLookupStrategy = JpaQueryLookupStrategy.create(entityManager, key, extractor, queryMethodEvaluationContextProvider, EscapeCharacter.DEFAULT);
        this.extractor = extractor;
        this.entityManager = entityManager;
        this.beanFactory = beanFactory;

        //添加不同类型资源库的查询策略
        queryLookupStrategyAdvices.add(new MybatisQueryLookupStrategy(beanFactory));
        log.info("查询查询策略初始化完成 {}，当前存在 {} 种查询方式", this.getClass().getSimpleName(), queryLookupStrategyAdvices.size());
    }

    public static QueryLookupStrategy create(EntityManager entityManager, BeanFactory beanFactory, Key key, QueryExtractor extractor, QueryMethodEvaluationContextProvider queryMethodEvaluationContextProvider) {
        return new QueryLookupStrategyFactories(entityManager, beanFactory,
                key, extractor, queryMethodEvaluationContextProvider);
    }

    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {
        //查找资源库策略
        Optional<QueryLookupStrategyAdvice> optionalQueryLookupStrategyAdvice = queryLookupStrategyAdvices
                .stream()
                .filter(queryLookupStrategyAdvice -> queryLookupStrategyAdvice.isEnabled(method, metadata))
                .findFirst();

        QueryLookupStrategy queryLookupStrategy = optionalQueryLookupStrategyAdvice.isPresent()?optionalQueryLookupStrategyAdvice.get():defaultQueryLookupStrategy;
        log.info("使用 {} 方式数据库持久化策略", "JPA 的 " + defaultQueryLookupStrategy.getClass().getSimpleName());
        return queryLookupStrategy.resolveQuery(method, metadata, factory, namedQueries);
    }
}
