package io.github.toquery.framework.system.autoconfig;

import io.github.toquery.framework.dao.EnableAppJpaRepositories;
import io.github.toquery.framework.system.properties.AppSystemProperties;
import io.github.toquery.framework.system.rest.*;
import io.github.toquery.framework.system.runner.AppSystemRunner;
import io.github.toquery.framework.system.service.*;
import io.github.toquery.framework.system.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
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
@MapperScan("io.github.toquery.framework.system.repository")
@EnableConfigurationProperties({AppSystemProperties.class})
@EntityScan(basePackages = "io.github.toquery.framework.system.entity")
@EnableAppJpaRepositories(basePackages = "io.github.toquery.framework.system")
@ConditionalOnProperty(prefix = AppSystemProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
//@EnableJpaRepositories(basePackages = {"io.github.toquery.framework.system.repository"}, repositoryFactoryBeanClass = AppJpaRepositoryFactoryBean.class)
public class AppSystemAutoConfiguration {

    @Autowired
    private PasswordEncoder passwordEncoder;


    public AppSystemAutoConfiguration() {
        log.info("自动装配 App System 自动配置");
    }


    @Bean
    @ConditionalOnMissingBean
    public SysConfigRest sysConfigRest() {
        return new SysConfigRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysDeptRest sysDeptRest() {
        return new SysDeptRest();
    }


    @Bean
    @ConditionalOnMissingBean
    public SysMenuRest sysMenuRest() {
        return new SysMenuRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysAreaRest sysAreaRest() {
        return new SysAreaRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysRoleRest sysRoleRest() {
        return new SysRoleRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysUserRest sysUserRest() {
        return new SysUserRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysDictRest sysDictRest() {
        return new SysDictRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysDictItemRest sysDictItemRest() {
        return new SysDictItemRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysPostRest sysPostRest() {
        return new SysPostRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysWorkRest sysWorkRest() {
        return new SysWorkRest();
    }


    @Bean
    @ConditionalOnMissingBean
    public ISysAreaService sysAreaService() {
        return new SysAreaServiceImpl();
    }


    @Bean
    @ConditionalOnMissingBean
    public ISysDeptService sysDeptService() {
        return new SysDeptServiceImpl();
    }


    @Bean
    @ConditionalOnMissingBean
    public ISysConfigService sysConfigService() {
        return new SysConfigServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysDictService sysDictService() {
        return new SysDictServiceImpl();
    }


    @Bean
    @ConditionalOnMissingBean
    public ISysDictItemService sysDictItemService() {
        return new SysDictItemServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysMenuService sysMenuService() {
        return new SysMenuServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysRoleService sysRoleService() {
        return new SysRoleServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysUserService sysUserService(ISysUserPermissionService sysUserPermissionService) {
        return new SysUserServiceImpl(passwordEncoder, sysUserPermissionService);
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysRoleMenuService sysRoleMenuService() {
        return new SysRoleMenuServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysUserPermissionService sysUserPermissionService(@Lazy ISysUserService sysUserService, @Lazy ISysRoleService sysRoleService, @Lazy ISysAreaService sysAreaService) {
        return new SysUserPermissionServiceImpl(sysUserService, sysRoleService, sysAreaService);
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysPostService sysPostService() {
        return new SysPostServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysWorkService sysWorkService(@Lazy ISysUserService sysUserService, @Lazy ISysDeptService sysDeptService, @Lazy ISysPostService sysPostService) {
        return new SysWorkServiceImpl(sysUserService, sysDeptService, sysPostService);
    }

    @Bean
    public ApplicationRunner getAppSystemRunner() {
        return new AppSystemRunner();
    }

}
