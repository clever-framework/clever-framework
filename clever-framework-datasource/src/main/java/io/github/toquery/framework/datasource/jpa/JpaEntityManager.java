
package io.github.toquery.framework.datasource.jpa;

import io.github.toquery.framework.dao.jpa.AppJpaRepositoryFactoryBean;
import io.github.toquery.framework.datasource.config.DynamicDataSourceRouter;
import io.github.toquery.framework.datasource.properties.AppDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableConfigurationProperties({JpaProperties.class})
@EnableJpaRepositories(
        value = "com.toquery.framework.example.modules.author.info.repository",
        entityManagerFactoryRef = "entityManagerFactoryBean",
        transactionManagerRef = "testTransactionManager",
        repositoryFactoryBeanClass = AppJpaRepositoryFactoryBean.class
)
public class JpaEntityManager {

    @Resource
    private JpaProperties jpaProperties;


    @Resource
    private AppDataSourceProperties appDataSourceProperties;

    public JpaEntityManager() {
        log.debug("JpaEntityManager init");
    }


    //    @Primary
    @Bean(name = "routingDataSource")
    public DynamicDataSourceRouter routingDataSource() {
        DynamicDataSourceRouter proxy = new DynamicDataSourceRouter();
        Map<String, DataSourceProperties> multiple = appDataSourceProperties.getMultiple();
        Map<Object, Object> targetDataSources = new HashMap<>(multiple.size());
        multiple.forEach((k, dataSourceProperties) -> {
            DataSourceBuilder<?> factory = DataSourceBuilder.create()
                    .type(dataSourceProperties.getType())
                    .driverClassName(dataSourceProperties.getDriverClassName())
                    .url(dataSourceProperties.getUrl())
                    .username(dataSourceProperties.getUsername())
                    .password(dataSourceProperties.getPassword());
            targetDataSources.put(k, factory.build());
        });
        proxy.setTargetDataSources(targetDataSources);
        return proxy;
    }


    //@Primary
//    @DependsOn("routingDataSource")
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
                .dataSource(routingDataSource())//关键：注入routingDataSource
                .properties(properties)
                .packages("com.toquery.framework.example.modules.author.info.entity")
                .persistenceUnit("myPersistenceUnit")
                .build();
    }


    @Bean
    public PlatformTransactionManager testTransactionManager(EntityManagerFactory entityManagerFactoryBean) {
        return new JpaTransactionManager(entityManagerFactoryBean);
    }


}
