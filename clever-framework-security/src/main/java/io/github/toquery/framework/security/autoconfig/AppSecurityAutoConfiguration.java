package io.github.toquery.framework.security.autoconfig;

import io.github.toquery.framework.security.auditor.AppAuditorAwareImpl;
import io.github.toquery.framework.security.handler.AppAuthenticationFailureHandler;
import io.github.toquery.framework.security.handler.AppAuthenticationSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
//@Configuration
//@DependsOn("io.github.toquery.framework.system.service.ISysUserService")
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
//@AutoConfigureAfter({AppSystemAutoConfiguration.class, AppBizLogAutoConfiguration.class})
@ComponentScan(basePackages = "io.github.toquery.framework.security")
//@ConditionalOnBean(value = {AppSystemAutoConfiguration.class, ISysUserService.class})
public class AppSecurityAutoConfiguration {

    public AppSecurityAutoConfiguration() {
        log.info("开始自动装配 App Security 自动化配置");
    }

    @Bean
    AuditorAware<Long> auditorProvider() {
        return new AppAuditorAwareImpl();
    }

   /*
    @Bean
    @ConditionalOnMissingBean
    public ISysLogService getSysLogService() {
        return new SysLogServiceImpl();
    }*/


    @Bean
    @ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
    AppAuthenticationSuccessHandler appAuthenticationSuccessHandler() {
        return new AppAuthenticationSuccessHandler();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    AppAuthenticationFailureHandler appAuthenticationFailureHandler() {
        return new AppAuthenticationFailureHandler();
    }
}
