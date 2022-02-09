
package com.toquery.framework.example.core.datasource;

import io.github.toquery.framework.dao.jpa.AppJpaRepositoryFactoryBean;
import io.github.toquery.framework.datasource.config.DynamicDataSourceRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Configuration
@EnableConfigurationProperties({JpaProperties.class})
@EnableJpaRepositories(
        value = "com.toquery.framework.example.modules.author.info.repository",
        entityManagerFactoryRef = "entityManagerFactoryBean",
//        transactionManagerRef = "testTransactionManager",
        repositoryFactoryBeanClass = AppJpaRepositoryFactoryBean.class
)
public class AppTestDataSourceConfig {

    @Resource
    private JpaProperties jpaProperties;

    @Resource
    private DynamicDataSourceRouter dynamicDataSourceRouter;

    public AppTestDataSourceConfig() {
        log.debug("SlaveDataSourceConfig init");
    }


    @DependsOn("routingDataSource")
    @Bean(name = "entityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        // 不明白为什么这里获取不到 application.yml 里的配置
        Map<String, String> properties = jpaProperties.getProperties();
        //要设置这个属性，实现 CamelCase -> UnderScore 的转换
        properties.put("hibernate.physical_naming_strategy",
                "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");

//        properties.put("hibernate.hbm2ddl.auto", "true");
//        properties.put("hibernate.dialect", "");
        return builder
                .dataSource(dynamicDataSourceRouter)//关键：注入routingDataSource
                .properties(properties)
                .packages("com.toquery.framework.example.modules.author.info.entity")
                .persistenceUnit("testPersistenceUnit")
                .build();
    }

//    @Bean
//    public PlatformTransactionManager testTransactionManager(EntityManagerFactory entityManagerFactoryBean) {
//        return new JpaTransactionManager(entityManagerFactoryBean);
//    }

}
