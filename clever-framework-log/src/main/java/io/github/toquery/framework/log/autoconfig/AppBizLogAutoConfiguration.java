package io.github.toquery.framework.log.autoconfig;

import io.github.toquery.framework.dao.EnableAppJpaRepositories;
import io.github.toquery.framework.log.properties.AppLogProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = AppLogProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
@ComponentScan(basePackages = "io.github.toquery.framework.log")
@EntityScan(basePackages = "io.github.toquery.framework.log.biz.entity")
@EnableAppJpaRepositories(basePackages = "io.github.toquery.framework.log")
public class AppBizLogAutoConfiguration {


    public AppBizLogAutoConfiguration() {
        log.info("开始自动装配App Log 配置");
    }


//    @Bean
//    public HibernateListenerConfigurer getAppAuditorHandler(){
//        return new HibernateListenerConfigurer();
//    }

}
