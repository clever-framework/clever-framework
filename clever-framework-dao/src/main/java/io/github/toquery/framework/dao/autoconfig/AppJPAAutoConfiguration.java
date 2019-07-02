package io.github.toquery.framework.dao.autoconfig;

import io.github.toquery.framework.dao.jpa.AppJpaRepositoryFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Slf4j
@Configuration
@ConditionalOnBean(DataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableJpaRepositories(basePackages = {"io.github.toquery.framework"}, repositoryFactoryBeanClass = AppJpaRepositoryFactoryBean.class)
public class AppJPAAutoConfiguration {

    public AppJPAAutoConfiguration() {
        log.info("初始化{}", this.getClass().getSimpleName());
    }

}
