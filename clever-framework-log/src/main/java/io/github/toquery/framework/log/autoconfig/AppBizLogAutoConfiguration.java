package io.github.toquery.framework.log.autoconfig;

import io.github.toquery.framework.dao.EnableAppJpaRepositories;
import io.github.toquery.framework.log.aspect.AppBizLogMethodAspect;
import io.github.toquery.framework.log.auditor.AppBizLogAnnotationHandler;
import io.github.toquery.framework.log.listener.AppLogAuthenticationFailureListener;
import io.github.toquery.framework.log.listener.AppLogAuthenticationSuccessListener;
import io.github.toquery.framework.log.properties.AppLogProperties;
import io.github.toquery.framework.log.rest.SysLogRest;
import io.github.toquery.framework.log.service.ISysLogService;
import io.github.toquery.framework.log.service.impl.SysLogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 *
 * @author toquery
 * @version 1
 */
@Slf4j
@EnableConfigurationProperties(AppLogProperties.class)
@EntityScan(basePackages = "io.github.toquery.framework.log.entity")
@EnableAppJpaRepositories(basePackages = "io.github.toquery.framework.log")
@ConditionalOnProperty(prefix = AppLogProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class AppBizLogAutoConfiguration {


    public AppBizLogAutoConfiguration() {
        log.info("开始自动装配 App Log 自动化配置");
    }

    @Bean
    @ConditionalOnMissingBean
    public AppBizLogMethodAspect getAppBizLogMethodAspect(){
        return new AppBizLogMethodAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysLogRest getSysLogRest(){
        return new SysLogRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysLogService getSysLogService() {
        return new SysLogServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public AppBizLogAnnotationHandler getAppBizLogAnnotationHandler(){
        return new AppBizLogAnnotationHandler();
    }


    @Bean
    public AppLogAuthenticationFailureListener appLogAuthenticationFailureListener() {
        return new AppLogAuthenticationFailureListener();
    }


    @Bean
    public AppLogAuthenticationSuccessListener appLogAuthenticationSuccessListener() {
        return new AppLogAuthenticationSuccessListener();
    }
}
