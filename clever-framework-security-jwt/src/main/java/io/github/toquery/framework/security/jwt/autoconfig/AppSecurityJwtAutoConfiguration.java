package io.github.toquery.framework.security.jwt.autoconfig;

import io.github.toquery.framework.security.jwt.JwtTokenUtil;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(AppSecurityJwtProperties.class)
@ComponentScan(basePackages = "io.github.toquery.framework.security.jwt")
public class AppSecurityJwtAutoConfiguration {

    @Resource
    private AppSecurityJwtProperties appSecurityJwtProperties;

    public AppSecurityJwtAutoConfiguration() {
        log.info("初始化 App Security Jwt 自动化配置");
    }


    @Bean
    @ConditionalOnMissingBean
    public JwtTokenUtil getJwtTokenUtil() {
        return new JwtTokenUtil(appSecurityJwtProperties);
    }
}
