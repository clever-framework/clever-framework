package io.github.toquery.framework.security.jwt.autoconfig;

import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(AppSecurityJwtProperties.class)
@ComponentScan(basePackages = "io.github.toquery.framework.security.jwt")
public class AppSecurityJwtAutoConfiguration {

    public AppSecurityJwtAutoConfiguration() {
        log.info("初始化 App Security Jwt 自动化配置");
    }

}
