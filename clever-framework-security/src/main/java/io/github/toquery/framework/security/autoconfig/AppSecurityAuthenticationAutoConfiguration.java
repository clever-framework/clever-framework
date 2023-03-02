package io.github.toquery.framework.security.autoconfig;

import io.github.toquery.framework.security.properties.AppSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

/**
 *
 */
@Slf4j
@ConditionalOnProperty(prefix = AppSecurityProperties.PREFIX, value = "enabled-daf", havingValue = "true", matchIfMissing = true)
public class AppSecurityAuthenticationAutoConfiguration {


    public AppSecurityAuthenticationAutoConfiguration() {
        log.debug("App 启用默认 Security 配置，包括 AuthenticationManager(defaultAuthenticationManager)");
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationManager defaultAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
