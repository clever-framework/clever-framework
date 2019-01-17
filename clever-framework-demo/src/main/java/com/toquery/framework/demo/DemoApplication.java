package com.toquery.framework.demo;

import io.github.toquery.framework.dao.EnableAppJpaRepositories;
import io.github.toquery.framework.dao.jpa.AppJpaRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author toquery
 * @version 1
 */
@SpringBootApplication
@EnableAppJpaRepositories
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
