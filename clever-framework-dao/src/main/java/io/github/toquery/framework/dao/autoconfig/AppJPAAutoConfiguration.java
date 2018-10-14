package io.github.toquery.framework.dao.autoconfig;

import io.github.toquery.framework.dao.jpa.AppJpaRepositoryFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@Configuration
//@ConditionalOnClass(DataSourceProperties.class)
@EnableJpaRepositories(basePackages = {"io.github.toquery.framework"}, repositoryFactoryBeanClass = AppJpaRepositoryFactoryBean.class)
public class AppJPAAutoConfiguration {

    public AppJPAAutoConfiguration() {
        log.info("初始化{}", this.getClass().getSimpleName());
    }

}
