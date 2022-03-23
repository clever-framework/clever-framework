package io.github.toquery.framework.datasource.autoconfig;

import io.github.toquery.framework.datasource.aop.DynamicDataSourceAspect;
import io.github.toquery.framework.datasource.jpa.DynamicDataSourceRouterConfiguration;
import io.github.toquery.framework.datasource.jpa.HibernateDefaultJpaConfiguration;
import io.github.toquery.framework.datasource.properties.AppDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Lazy(value = false)
//@Configuration
@Import({
        HibernateDefaultJpaConfiguration.class,
        DynamicDataSourceRouterConfiguration.class
})
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties(AppDataSourceProperties.class)
@ConditionalOnProperty(prefix = AppDataSourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class AppDataSourceAutoConfiguration {

    public AppDataSourceAutoConfiguration() {
        log.info("自动装配 App DataSource 配置");
    }

    @Bean
    public DynamicDataSourceAspect getDynamicDataSourceAspect() {
        return new DynamicDataSourceAspect();
    }
}
