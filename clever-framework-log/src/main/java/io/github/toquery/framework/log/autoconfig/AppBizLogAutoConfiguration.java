package io.github.toquery.framework.log.autoconfig;

import io.github.toquery.framework.log.event.AppHibernateListenerConfigurer;
import io.github.toquery.framework.log.event.listener.AppBizLogDeleteEventListener;
import io.github.toquery.framework.log.event.listener.AppBizLogMergeEventListener;
import io.github.toquery.framework.log.event.listener.AppBizLogPersistEventListener;
import io.github.toquery.framework.log.properties.AppLogProperties;
import io.github.toquery.framework.system.autoconfig.AppSystemAutoConfiguration;
import io.github.toquery.framework.system.properties.AppSystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author toquery
 * @version 1
 */
@Slf4j
//@Configuration
@Import(AppHibernateListenerConfigurer.class)
@EnableConfigurationProperties(AppLogProperties.class)
//@AutoConfigureAfter({AppSystemAutoConfiguration.class})
@ConditionalOnProperty(prefix = AppLogProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
@ComponentScan(basePackages = "io.github.toquery.framework.log")
public class AppBizLogAutoConfiguration {


    public AppBizLogAutoConfiguration() {
        log.info("开始自动装配 App Log 自动化配置");
    }


//    @Bean
//    public HibernateListenerConfigurer getAppAuditorHandler(){
//        return new HibernateListenerConfigurer();
//    }

    @Bean
    public AppBizLogPersistEventListener getAppBizLogPersistEventListener() {
        return new AppBizLogPersistEventListener();
    }

    @Bean
    public AppBizLogMergeEventListener getAppBizLogMergeEventListener() {
        return new AppBizLogMergeEventListener();
    }

    @Bean
    public AppBizLogDeleteEventListener getAppBizLogDeleteEventListener() {
        return new AppBizLogDeleteEventListener();
    }


//    @PersistenceUnit
//    private EntityManagerFactory entityManagerFactory;
//
//
//
//    @PostConstruct
//    protected void init() {
//        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
//        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
//        log.debug("加载 自定义 Listener AppBizLogPersistEventListener");
//        registry.getEventListenerGroup(EventType.PERSIST).appendListener(getAppBizLogPersistEventListener());
//    }
}
