package com.toquery.framework.demo;

import io.github.toquery.framework.dao.jpa.AppJpaRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author toquery
 * @version 1
 */
@EnableJpaRepositories(basePackages = {"io.github.toquery.framework", "com.toquery.framework.demo"}, repositoryFactoryBeanClass = AppJpaRepositoryFactoryBean.class)
@EntityScan("com.toquery.framework.demo.entity")
//@EnableJpaRepositories
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
