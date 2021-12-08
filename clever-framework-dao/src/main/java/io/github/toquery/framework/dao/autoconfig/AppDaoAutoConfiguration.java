package io.github.toquery.framework.dao.autoconfig;

import io.github.toquery.framework.dao.properties.AppDaoProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Slf4j
//@Configuration
@ConditionalOnBean(DataSource.class)
@Import(HibernateListenerConfigurer.class)
@EnableConfigurationProperties(AppDaoProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
// @EnableJpaRepositories(basePackages = {"io.github.toquery.framework"}, repositoryFactoryBeanClass = AppJpaRepositoryFactoryBean.class)
public class AppDaoAutoConfiguration {

    public AppDaoAutoConfiguration() {
        log.info("初始化 App Dao 模块 {}", this.getClass().getSimpleName());
    }


    /*
    @Bean
    public Interceptor appEntityAuditInterceptor(SqlSessionFactory sqlSessionFactory) {
        AppEntityAuditInterceptor appEntityAuditInterceptor = new AppEntityAuditInterceptor();
        sqlSessionFactory.getConfiguration().addInterceptor(appEntityAuditInterceptor);
        return appEntityAuditInterceptor;
    }
    */
}
