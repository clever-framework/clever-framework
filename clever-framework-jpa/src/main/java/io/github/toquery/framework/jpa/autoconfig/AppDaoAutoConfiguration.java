package io.github.toquery.framework.jpa.autoconfig;

import io.github.toquery.framework.jpa.properties.AppDaoProperties;
import io.github.toquery.framework.jpa.properties.AppMybatisProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@Slf4j
//@Configuration
//@ConditionalOnBean(DataSource.class)
@Import(HibernateListenerConfigurer.class)
@EnableConfigurationProperties({AppDaoProperties.class, AppMybatisProperties.class})
//@AutoConfigureAfter(DataSourceAutoConfiguration.class)
// @EnableJpaRepositories(basePackages = {"io.github.toquery.framework"}, repositoryFactoryBeanClass = AppJpaRepositoryFactoryBean.class)
public class AppDaoAutoConfiguration {

    public AppDaoAutoConfiguration() {
        log.info("自动装配 App Dao 模块 {}", this.getClass().getSimpleName());
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
