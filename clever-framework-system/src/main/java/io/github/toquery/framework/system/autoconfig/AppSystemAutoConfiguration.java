package io.github.toquery.framework.system.autoconfig;

import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.dao.EnableAppJpaRepositories;
import io.github.toquery.framework.system.properties.AppSystemProperties;
import io.github.toquery.framework.system.rest.SysAreaRest;
import io.github.toquery.framework.system.rest.SysConfigRest;
import io.github.toquery.framework.system.rest.SysDeptRest;
import io.github.toquery.framework.system.rest.SysDictRest;
import io.github.toquery.framework.system.rest.SysMenuRest;
import io.github.toquery.framework.system.rest.SysRoleRest;
import io.github.toquery.framework.system.rest.SysUserRest;
import io.github.toquery.framework.system.runner.AppSystemRunner;
import io.github.toquery.framework.system.service.*;
import io.github.toquery.framework.system.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 相关业务bean 不支持通过new的方式创建，只能扫描包方式创建
 *
 * @author toquery
 * @version 1
 */
@Slf4j
//@Configuration
//@EnableAppRepositoryRest
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties({AppSystemProperties.class})
@EntityScan(basePackages = "io.github.toquery.framework.system.entity")
@EnableAppJpaRepositories(basePackages = "io.github.toquery.framework.system")
@ConditionalOnProperty(prefix = AppSystemProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
//@EnableJpaRepositories(basePackages = {"io.github.toquery.framework.system.repository"}, repositoryFactoryBeanClass = AppJpaRepositoryFactoryBean.class)
public class AppSystemAutoConfiguration {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public AppSystemAutoConfiguration() {
        log.info("自动装配 App System 自动配置");
    }


    @Bean
    @ConditionalOnMissingBean
    public SysConfigRest getSysConfigRest() {
        return new SysConfigRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysDeptRest getSysDeptRest() {
        return new SysDeptRest();
    }


    @Bean
    @ConditionalOnMissingBean
    public SysMenuRest getSysMenuRest() {
        return new SysMenuRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysAreaRest getSysAreaRest() {
        return new SysAreaRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysRoleRest getSysRoleRest() {
        return new SysRoleRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysUserRest getSysUserRest() {
        return new SysUserRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysDictRest getSysDictRest() {
        return new SysDictRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysAreaService getISysAreaService() {
        return new SysAreaServiceImpl();
    }


    @Bean
    @ConditionalOnMissingBean
    public ISysDeptService getISysDeptService() {
        return new SysDeptServiceImpl();
    }


    @Bean
    @ConditionalOnMissingBean
    public ISysConfigService getSysConfigService() {
        return new SysConfigServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysDictService getISysDictService() {
        return new SysDictServiceImpl();
    }


    @Bean
    @ConditionalOnMissingBean
    public ISysDictItemService getISysDictItemService() {
        return new SysDictItemServiceImpl();
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

    @Bean
    @ConditionalOnMissingBean
    public ISysRoleMenuService getSysRoleMenuService() {
        return new SysRoleMenuServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysUserPermissionService getSysUserPermissionServiceImpl() {
        return new SysUserPermissionServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApplicationRunner getAppSystemRunner() {
        return new AppSystemRunner();

    }

}
