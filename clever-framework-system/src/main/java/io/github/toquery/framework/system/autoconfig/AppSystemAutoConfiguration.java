package io.github.toquery.framework.system.autoconfig;

import io.github.toquery.framework.dao.EnableAppJpaRepositories;
import io.github.toquery.framework.system.properties.AppSystemProperties;
import io.github.toquery.framework.system.service.ISysConfigService;
import io.github.toquery.framework.system.service.ISysMenuService;
import io.github.toquery.framework.system.service.ISysRoleService;
import io.github.toquery.framework.system.service.ISysUserService;
import io.github.toquery.framework.system.service.impl.SysConfigServiceImpl;
import io.github.toquery.framework.system.service.impl.SysMenuServiceImpl;
import io.github.toquery.framework.system.service.impl.SysRoleServiceImpl;
import io.github.toquery.framework.system.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({AppSystemProperties.class})
@EntityScan(basePackages = "io.github.toquery.framework.system.entity")
@EnableAppJpaRepositories(basePackages = "io.github.toquery.framework.system.repository")
@ConditionalOnProperty(prefix = AppSystemProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class AppSystemAutoConfiguration {

    public AppSystemAutoConfiguration() {
        log.info("开始自动装配 App System 自动配置");
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysConfigService getSysConfigService() {
        return new SysConfigServiceImpl();
    }


    @Bean
    @ConditionalOnMissingBean
    public ISysMenuService getSysMenuService() {
        return new SysMenuServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysRoleService getSysRoleService() {
        return new SysRoleServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysUserService getSysUserService() {
        return new SysUserServiceImpl();
    }
}
