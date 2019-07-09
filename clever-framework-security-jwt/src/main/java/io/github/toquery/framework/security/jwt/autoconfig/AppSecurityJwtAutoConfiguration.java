package io.github.toquery.framework.security.jwt.autoconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = "io.github.toquery.framework.security.jwt")
public class AppSecurityJwtAutoConfiguration {

    public AppSecurityJwtAutoConfiguration() {
        log.info("开始自动装配 App Security Jwt 配置");
    }

}