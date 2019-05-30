package io.github.toquery.framework.security.config;

import io.github.toquery.framework.dao.EnableAppJpaRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = "io.github.toquery.framework.security")
@EntityScan(basePackages = "io.github.toquery.framework.security.domain")
@EnableAppJpaRepositories(basePackages = "io.github.toquery.framework.security")
public class AppSecurityAutoConfiguration {

    public AppSecurityAutoConfiguration() {
        log.info("开始自动装配App Security 配置");
    }

}
