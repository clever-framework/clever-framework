package io.github.toquery.framework.dao.jpa;

import io.github.toquery.framework.dao.repository.impl.AppJpaBaseRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

import javax.persistence.EntityManager;

/**
 * 自定义的JpaRepositoryFactory，用于生成JpaRepository
 */
@Slf4j
public class AppJpaRepositoryFactory extends JpaRepositoryFactory {

    protected final EntityManager entityManager;

    protected final QueryExtractor extractor;

    protected BeanFactory beanFactory;

    public AppJpaRepositoryFactory(EntityManager entityManager, BeanFactory beanFactory) {
        super(entityManager);
        this.beanFactory = beanFactory;
        //设置当前类的实体管理器
        this.entityManager = entityManager;
        this.extractor = PersistenceProvider.fromEntityManager(entityManager);
        log.info("初始化自定义JPA仓库工厂");
    }

    /**
     * getTargetRepository
     */
    @Override
    protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
        log.info("创建实体 {} 的 DAO 实现类 {} ", information.getDomainType().getName(), information.getRepositoryBaseClass().getName());
        return super.getTargetRepository(information, entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        //自定接口实现方法的基类
        return AppJpaBaseRepositoryImpl.class;
    }
}
