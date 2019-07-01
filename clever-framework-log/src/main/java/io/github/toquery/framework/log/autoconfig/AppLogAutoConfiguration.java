package io.github.toquery.framework.log.autoconfig;

import io.github.toquery.framework.dao.EnableAppJpaRepositories;
import io.github.toquery.framework.dao.audit.AppAuditorHandler;
import io.github.toquery.framework.log.auditor.AppAuditorBizLogHandler;
import io.github.toquery.framework.log.dao.SysLogRepository;
import io.github.toquery.framework.log.properties.AppLogProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = AppLogProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
@ComponentScan(basePackages = "io.github.toquery.framework.log")
@EntityScan(basePackages = "io.github.toquery.framework.log.entity")
@EnableAppJpaRepositories(basePackages = "io.github.toquery.framework.log")
public class AppLogAutoConfiguration {


    public AppLogAutoConfiguration() {
        log.info("开始自动装配App Log 配置");
    }


//    @Bean
//    public AppAuditorHandler getAppAuditorHandler(){
//        return new AppAuditorBizLogHandler();
//    }

}
