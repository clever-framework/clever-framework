package io.github.toquery.framework.files.autoconfig;

import io.github.toquery.framework.dao.EnableAppJpaRepositories;
import io.github.toquery.framework.files.properties.AppFilesProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(AppFilesProperties.class)
@ComponentScan(basePackages = "io.github.toquery.framework.files")
@EntityScan(basePackages = "io.github.toquery.framework.files.domain")
@EnableAppJpaRepositories(basePackages = "io.github.toquery.framework.files")
public class AppFilesAutoConfiguration {
    public AppFilesAutoConfiguration() {
        log.info("初始化 App Files 自动配置");
    }
}
