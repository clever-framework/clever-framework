package io.github.toquery.framework.core.autoconfig;

import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.core.security.AppSecurityDefaultIgnoring;
import io.github.toquery.framework.core.security.AppSecurityIgnoringHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Lazy(value = false)
//@Configuration
@EnableConfigurationProperties(AppProperties.class)
@ConditionalOnProperty(prefix = AppProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class AppCoreAutoConfiguration {

    public AppCoreAutoConfiguration() {
        log.info("自动装配 App Core 模块");
    }

    @Bean
    public AppSecurityIgnoringHandlerAdapter getAppSecurityIgnoringHandler(){
        return new AppSecurityIgnoringHandlerAdapter();
    }

    @Bean
    @ConditionalOnMissingBean
    public AppSecurityDefaultIgnoring getAppSecurityDefaultIgnoring(){
        return new AppSecurityDefaultIgnoring();
    }
}
