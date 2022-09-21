package io.github.toquery.framework.jpa.autoconfig;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

/**
 * jpa 配置忽略空值
 *
 * @author toquery
 */
@Slf4j
public class HibernateListenerConfigurer {

//    @PersistenceUnit
//    private EntityManagerFactory entityManagerFactory;

    public HibernateListenerConfigurer() {
        log.info("HibernateListenerConfigurer init");
    }

    @PostConstruct
    protected void init() {
//        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
//        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
//        registry.getEventListenerGroup(EventType.MERGE).clearListeners();
//        registry.getEventListenerGroup(EventType.MERGE).prependListener(IgnoreNullEventListener.INSTANCE);
    }
}
