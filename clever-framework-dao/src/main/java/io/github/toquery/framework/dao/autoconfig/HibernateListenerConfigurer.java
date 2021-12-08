package io.github.toquery.framework.dao.autoconfig;

import io.github.toquery.framework.dao.jpa.listener.IgnoreNullEventListener;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * jpa 配置忽略空值
 *
 * @author toquery
 */
@Slf4j
public class HibernateListenerConfigurer {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public HibernateListenerConfigurer() {
        log.info("HibernateListenerConfigurer init");
    }

    @PostConstruct
    protected void init() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.MERGE).clearListeners();
        registry.getEventListenerGroup(EventType.MERGE).prependListener(IgnoreNullEventListener.INSTANCE);
    }
}
