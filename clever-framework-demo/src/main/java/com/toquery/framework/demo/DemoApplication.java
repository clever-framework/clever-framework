package com.toquery.framework.demo;


import io.github.toquery.framework.dao.EnableAppJpaRepositories;
import io.github.toquery.framework.data.rest.annotation.EnableAppRepositoryRest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author toquery
 * @version 1
 */
@SpringBootApplication
@EnableAppJpaRepositories
@EnableAppRepositoryRest
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
