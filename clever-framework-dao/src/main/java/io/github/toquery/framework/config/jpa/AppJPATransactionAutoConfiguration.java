package io.github.toquery.framework.config.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "spring.jpa", name = "enabled", matchIfMissing = true, havingValue = "true")
@EnableTransactionManagement(proxyTargetClass = true)
public class AppJPATransactionAutoConfiguration {

    public AppJPATransactionAutoConfiguration() {
        log.info("初始化 {}", this.getClass().getSimpleName());
    }

    /**
     * 创建JPA事务管理器，事务管理器的类名称为：transactionManager
     *
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        log.info("创建事务管理器: {} ", jpaTransactionManager.getClass().getName());
        return jpaTransactionManager;
    }
}
