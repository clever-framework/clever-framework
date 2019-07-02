package io.github.toquery.framework.log.event;

import io.github.toquery.framework.log.event.listener.AppBizLogPersistEventListener;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Component
public class AppHibernateListenerConfigurer {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public AppHibernateListenerConfigurer() {
        log.info("初始化 App Biz Log Hibernate Listener 模块");
    }

    @PostConstruct
    protected void init() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);

        log.debug("加载 自定义 Listener AppBizLogPersistEventListener");
        registry.getEventListenerGroup(EventType.PERSIST).appendListener(AppBizLogPersistEventListener.INSTANCE);
    }
}