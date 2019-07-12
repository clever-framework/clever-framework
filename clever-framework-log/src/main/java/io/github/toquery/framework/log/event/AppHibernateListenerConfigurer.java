package io.github.toquery.framework.log.event;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.DeleteEventListener;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.MergeEventListener;
import org.hibernate.event.spi.PersistEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * AppBizLogAutoConfiguration 启用才会注入
 *
 * @author toquery
 * @version 1
 */
@Slf4j
public class AppHibernateListenerConfigurer {

    @Autowired
    private PersistEventListener[] persistEventListeners;

    @Autowired
    private MergeEventListener[] mergeEventListeners;

    @Autowired
    private DeleteEventListener[] deleteEventListeners;

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
        registry.getEventListenerGroup(EventType.PERSIST).appendListeners(persistEventListeners);

        log.debug("加载 自定义 Listener AppBizLogMergeEventListener");
        registry.getEventListenerGroup(EventType.MERGE).appendListeners(mergeEventListeners);

        log.debug("加载 自定义 Listener AppBizLogDeleteEventListener");
        registry.getEventListenerGroup(EventType.DELETE).appendListeners(deleteEventListeners);
    }
}