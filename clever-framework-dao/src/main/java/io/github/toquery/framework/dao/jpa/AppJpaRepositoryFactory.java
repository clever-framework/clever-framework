package io.github.toquery.framework.dao.jpa;

import io.github.toquery.framework.dao.jpa.lookup.QueryLookupStrategyFactories;
import io.github.toquery.framework.dao.repository.impl.AppJpaBaseRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * 自定义的JpaRepositoryFactory，用于生成JpaRepository
 */
@Slf4j
public class AppJpaRepositoryFactory extends JpaRepositoryFactory {

    private EntityManager entityManager;

    protected final QueryExtractor extractor;

    protected BeanFactory beanFactory;

    public AppJpaRepositoryFactory(EntityManager entityManager, BeanFactory beanFactory) {
        super(entityManager);
        this.beanFactory = beanFactory;
        //设置当前类的实体管理器
        this.entityManager = entityManager;
        this.extractor = PersistenceProvider.fromEntityManager(entityManager);
    }

    /**
     * 原方法
     */
    @Override
    protected SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
        log.info("创建实体 {} 的DAO实现类: {} ", information.getDomainType().getName(), information.getRepositoryBaseClass().getName());
        return super.getTargetRepository(information, entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        //自定接口实现方法的基类
        return AppJpaBaseRepositoryImpl.class;
    }

    /**
     * 查找使用自定义注解，然后mybatis执行
     */
    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(@Nullable QueryLookupStrategy.Key key, EvaluationContextProvider evaluationContextProvider) {
        QueryLookupStrategy queryLookupStrategy = QueryLookupStrategyFactories.create(entityManager, beanFactory, key, extractor, evaluationContextProvider);
        return Optional.of(queryLookupStrategy);
    }

}
