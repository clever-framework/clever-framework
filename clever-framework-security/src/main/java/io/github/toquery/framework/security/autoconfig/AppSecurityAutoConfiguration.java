package io.github.toquery.framework.security.autoconfig;

import io.github.toquery.framework.security.config.AppSecurityConfig;
import io.github.toquery.framework.security.exception.AppSecurityExceptionAdvice;
import io.github.toquery.framework.security.ignoring.AppSecurityJwtIgnoring;
import io.github.toquery.framework.security.properties.AppSecurityAdminProperties;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import io.github.toquery.framework.security.provider.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtEncoder;

/**
 * <a href="https://juejin.cn/post/6985893815500406791">https://juejin.cn/post/6985893815500406791</a>
 *
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@Import({AppSecurityConfig.class, AppSecurityJwtIgnoring.class})
@EnableConfigurationProperties({AppSecurityProperties.class, AppSecurityAdminProperties.class})
public class AppSecurityAutoConfiguration {

    public AppSecurityAutoConfiguration() {
        log.info("自动装配 App Security 自动化配置");
    }

    @Bean
    @ConditionalOnMissingBean
    public AppSecurityExceptionAdvice appSecurityJwtExceptionAdvice() {
        return new AppSecurityExceptionAdvice();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtTokenProvider jwtTokenProvider(JwtEncoder encoder, AppSecurityAdminProperties appSecurityJwtProperties) {
        return new JwtTokenProvider(encoder, appSecurityJwtProperties);
    }

}
