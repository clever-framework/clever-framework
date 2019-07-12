package io.github.toquery.framework.security.autoconfig;

import io.github.toquery.framework.dao.EnableAppJpaRepositories;
import io.github.toquery.framework.security.auditor.AppAuditorAwareImpl;
import io.github.toquery.framework.system.service.ISysLogService;
import io.github.toquery.framework.system.service.impl.SysLogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@ComponentScan(basePackages = "io.github.toquery.framework.security")
public class AppSecurityAutoConfiguration {

    public AppSecurityAutoConfiguration() {
        log.info("开始自动装配 App Security 自动化配置");
    }

    @Bean
    AuditorAware<Long> auditorProvider() {
        return new AppAuditorAwareImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysLogService getSysLogService() {
        return new SysLogServiceImpl();
    }

}
