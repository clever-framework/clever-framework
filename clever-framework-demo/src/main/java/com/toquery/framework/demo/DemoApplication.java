package com.toquery.framework.demo;


import io.github.toquery.framework.dao.EnableAppJpaRepositories;
import io.github.toquery.framework.data.rest.annotation.EnableAppRepositoryRest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * @author toquery
 * @version 1
 */
@SpringBootApplication
@EntityScan(basePackages = {"io.github.toquery.framework.security.domain","com.toquery.framework.demo.entity"})
@EnableAppJpaRepositories(basePackages = {"io.github.toquery.framework.security","com.toquery.framework.demo"})
@EnableAppRepositoryRest
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
