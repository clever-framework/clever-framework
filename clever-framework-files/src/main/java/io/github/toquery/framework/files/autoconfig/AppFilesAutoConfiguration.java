package io.github.toquery.framework.files.autoconfig;

import io.github.toquery.framework.jpa.EnableAppJpaRepositories;
import io.github.toquery.framework.files.properties.AppFilesProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
//@Configuration
@EnableConfigurationProperties(AppFilesProperties.class)
@ComponentScan(basePackages = "io.github.toquery.framework.files")
@EntityScan(basePackages = "io.github.toquery.framework.files.entity")
@EnableAppJpaRepositories(basePackages = "io.github.toquery.framework.files")
@ConditionalOnProperty(prefix = AppFilesProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
//@EnableJpaRepositories(basePackages = {"io.github.toquery.framework.files"}, repositoryFactoryBeanClass = AppJpaRepositoryFactoryBean.class)
public class AppFilesAutoConfiguration {
    public AppFilesAutoConfiguration() {
        log.info("自动装配 App Files 自动配置");
    }
}
