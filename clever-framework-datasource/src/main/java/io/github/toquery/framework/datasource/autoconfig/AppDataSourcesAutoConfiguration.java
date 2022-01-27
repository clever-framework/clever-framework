package io.github.toquery.framework.datasource.autoconfig;

import io.github.toquery.framework.datasource.aop.DynamicDataSourceAspect;
import io.github.toquery.framework.datasource.properties.AppDataSourcesProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
//@Configuration
@EnableConfigurationProperties(AppDataSourcesProperties.class)
//@ConditionalOnProperty(prefix = AppDataSourcesProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class AppDataSourcesAutoConfiguration {

    public AppDataSourcesAutoConfiguration() {
        log.info("加载 AppDataSourcesAutoConfiguration");
    }

    @Bean
    public DynamicDataSourceAspect getDynamicDataSourceAspect(){
        return new DynamicDataSourceAspect();
    }
}
