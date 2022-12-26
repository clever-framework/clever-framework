package io.github.toquery.framework.system.autoconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.core.security.userdetails.AppUserDetailService;
import io.github.toquery.framework.system.DelegatingSysUserOnline;
import io.github.toquery.framework.system.mapper.SysPermissionMapper;
import io.github.toquery.framework.system.properties.AppSystemProperties;
import io.github.toquery.framework.system.rest.*;
import io.github.toquery.framework.system.runner.AppSystemRunner;
import io.github.toquery.framework.system.service.*;
import io.github.toquery.framework.system.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@MapperScan("io.github.toquery.framework.system.mapper")
@EnableConfigurationProperties({AppSystemProperties.class})
@EntityScan(basePackages = "io.github.toquery.framework.system.entity")
//@EnableAppJpaRepositories(basePackages = "io.github.toquery.framework.system")
@ConditionalOnProperty(prefix = AppSystemProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
//@EnableJpaRepositories(basePackages = {"io.github.toquery.framework.system.repository"}, repositoryFactoryBeanClass = AppJpaRepositoryFactoryBean.class)
public class AppSystemAutoConfiguration {


    public AppSystemAutoConfiguration() {
        log.info("自动装配 App System 自动配置");
    }


    @Bean
    @ConditionalOnMissingBean
    public AuthenticationRest authenticationRest(AppProperties appProperties,
                                                 ISysUserService sysUserService,
                                                 AppUserDetailService appUserDetailsService,
                                                 DelegatingSysUserOnline sysUserOnlineHandler,
                                                 AuthenticationManager authenticationManager) {
        return new AuthenticationRest(appProperties, sysUserService, sysUserOnlineHandler, appUserDetailsService, authenticationManager);
    }

    @Bean
    @ConditionalOnProperty(prefix = AppSystemProperties.PREFIX, name = "register", havingValue = "true")
    public UserRegisterRest userRegisterRest(PasswordEncoder passwordEncoder, ISysUserService sysUserService) {
        log.debug("系统当前配置开启用户注册");
        return new UserRegisterRest(passwordEncoder, sysUserService);
    }


    @Bean
    @ConditionalOnMissingBean
    public DelegatingSysUserOnline delegatingSysUserOnline(ISysUserOnlineService sysUserOnlineService) {
        return new DelegatingSysUserOnline(sysUserOnlineService);
    }

    @Bean
    @ConditionalOnMissingBean
    public SysConfigRest sysConfigRest(ISysConfigService sysConfigService) {
        return new SysConfigRest(sysConfigService);
    }

    @Bean
    @ConditionalOnMissingBean
    public SysDeptRest sysDeptRest(ISysDeptService sysDeptService) {
        return new SysDeptRest(sysDeptService);
    }


    @Bean
    @ConditionalOnMissingBean
    public SysMenuRest sysMenuRest(ISysMenuService sysMenuService) {
        return new SysMenuRest(sysMenuService);
    }

    @Bean
    @ConditionalOnMissingBean
    public SysAreaRest sysAreaRest(ISysAreaService sysAreaService) {
        return new SysAreaRest(sysAreaService);
    }

    @Bean
    @ConditionalOnMissingBean
    public SysRoleRest sysRoleRest(ISysRoleService sysRoleService) {
        return new SysRoleRest(sysRoleService);
    }

    @Bean
    @ConditionalOnMissingBean
    public SysUserRest sysUserRest(AppProperties appProperties, PasswordEncoder passwordEncoder, ObjectMapper objectMapper, ISysUserService sysUserService) {
        return new SysUserRest(appProperties, passwordEncoder, objectMapper, sysUserService);
    }

    @Bean
    @ConditionalOnMissingBean
    public SysUserOnlineRest sysUserOnlineRest() {
        return new SysUserOnlineRest();
    }

    @Bean
    @ConditionalOnMissingBean
    public SysDictRest sysDictRest(ISysDictService sysDictService) {
        return new SysDictRest(sysDictService);
    }

    @Bean
    @ConditionalOnMissingBean
    public SysDictItemRest sysDictItemRest(ISysDictItemService sysDictItemService) {
        return new SysDictItemRest(sysDictItemService);
    }

    @Bean
    @ConditionalOnMissingBean
    public SysPostRest sysPostRest(ISysPostService sysPostService) {
        return new SysPostRest(sysPostService);
    }

    @Bean
    @ConditionalOnMissingBean
    public SysWorkRest sysWorkRest(ISysWorkService sysWorkService) {
        return new SysWorkRest(sysWorkService);
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
    public ISysMenuService sysMenuService(ApplicationContext applicationContext) {
        return new SysMenuServiceImpl(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysRoleService sysRoleService() {
        return new SysRoleServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysUserService sysUserService(PasswordEncoder passwordEncoder, ISysPermissionService sysUserPermissionService) {
        return new SysUserServiceImpl(passwordEncoder, sysUserPermissionService);
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysUserOnlineService sysUserOnlineService() {
        return new SysUserOnlineServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysRoleMenuService sysRoleMenuService() {
        return new SysRoleMenuServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISysPermissionService sysUserPermissionService(@Lazy ISysUserService sysUserService, @Lazy ISysRoleService sysRoleService, @Lazy ISysAreaService sysAreaService, @Lazy SysPermissionMapper sysPermissionMapper) {
        return new SysPermissionServiceImpl(sysUserService, sysRoleService, sysAreaService, sysPermissionMapper);
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
    public ApplicationRunner getAppSystemRunner(AppSystemProperties appSystemProperties, ISysMenuService sysMenuService) {
        return new AppSystemRunner(appSystemProperties, sysMenuService);
    }

}
