package io.github.toquery.framework.front.autoconfig;

import io.github.toquery.framework.front.connfig.AppFrontConfigurer;
import io.github.toquery.framework.front.connfig.AppFrontSecurityConfig;
import io.github.toquery.framework.front.properties.AppFrontProperties;
import io.github.toquery.framework.front.rest.AppFrontConfigRest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
//@Import({AppFrontConfigurer.class})
@Import({AppFrontConfigurer.class, AppFrontSecurityConfig.class})
@EnableConfigurationProperties({AppFrontProperties.class})
@ConditionalOnProperty(prefix = AppFrontProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class AppFrontAutoConfiguration {

    public AppFrontAutoConfiguration() {
        log.info("自动装配 App Front 自动配置");
    }

    @Bean
    @ConditionalOnMissingBean
    public AppFrontConfigRest getAppFrontConfigRest(){
        return new AppFrontConfigRest();
    }
}
