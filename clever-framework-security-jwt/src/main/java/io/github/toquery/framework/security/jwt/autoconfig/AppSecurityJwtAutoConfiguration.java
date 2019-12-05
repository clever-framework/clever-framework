package io.github.toquery.framework.security.jwt.autoconfig;

import io.github.toquery.framework.log.autoconfig.AppBizLogAutoConfiguration;
import io.github.toquery.framework.security.autoconfig.AppSecurityAutoConfiguration;
import io.github.toquery.framework.security.jwt.JwtTokenUtil;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.system.autoconfig.AppSystemAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
//@AutoConfigureAfter({AppSystemAutoConfiguration.class,  AppSecurityAutoConfiguration.class, AppBizLogAutoConfiguration.class})
@EnableConfigurationProperties(AppSecurityJwtProperties.class)
@ComponentScan(basePackages = "io.github.toquery.framework.security.jwt")
@ConditionalOnProperty(prefix = AppSecurityJwtProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class AppSecurityJwtAutoConfiguration {

    @Resource
    private AppSecurityJwtProperties appSecurityJwtProperties;

    public AppSecurityJwtAutoConfiguration() {
        log.info("初始化 App Security Jwt 自动化配置");
    }


    @Bean
    @ConditionalOnMissingBean
    public JwtTokenUtil getJwtTokenUtil() {
        return new JwtTokenUtil(appSecurityJwtProperties);
    }
}
