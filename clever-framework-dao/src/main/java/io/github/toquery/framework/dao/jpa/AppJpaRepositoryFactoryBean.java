package io.github.toquery.framework.dao.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * 自定义的JpaRepositoryFactoryBean类，用于生成自定义的 JpaRepositoryFactory
 */

@Slf4j
public class AppJpaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends JpaRepositoryFactoryBean<T, S, ID> {

    private BeanFactory beanFactory;

    public AppJpaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(@Nullable EntityManager entityManager) {
        return new AppJpaRepositoryFactory(entityManager, beanFactory);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        super.setBeanFactory(beanFactory);
    }
}
