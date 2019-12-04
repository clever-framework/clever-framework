package io.github.toquery.framework.system.autoconfig;

import io.github.toquery.framework.system.properties.AppSystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * 相关业务bean 不支持通过new的方式创建，只能扫描包方式创建
 *
 * @author toquery
 * @version 1
 */
@Slf4j
//@Configuration
//@EnableAppRepositoryRest
@ComponentScan("io.github.toquery.framework.system")
@EnableConfigurationProperties({AppSystemProperties.class})
@EntityScan(basePackages = "io.github.toquery.framework.system.entity")
//@EnableAppJpaRepositories(basePackages = "io.github.toquery.framework.system")
@ConditionalOnProperty(prefix = AppSystemProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class AppSystemAutoConfiguration {

    public AppSystemAutoConfiguration() {
        log.info("开始自动装配 App System 自动配置");
    }

    /*
    @Bean
    @ConditionalOnMissingBean
    public SysConfigServiceImpl getSysConfigService() {
        return new SysConfigServiceImpl();
    }
    @Bean
    @ConditionalOnMissingBean
    public SysMenuServiceImpl getSysMenuService() {
        return new SysMenuServiceImpl();
    }
    @Bean
    @ConditionalOnMissingBean
    public SysRoleServiceImpl getSysRoleService() {
        return new SysRoleServiceImpl();
    }
    @Bean
    @ConditionalOnMissingBean
    public SysUserServiceImpl getSysUserService() {
        return new SysUserServiceImpl();
    }
     */
}
