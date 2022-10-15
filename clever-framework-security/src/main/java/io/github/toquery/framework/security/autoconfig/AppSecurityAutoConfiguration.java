package io.github.toquery.framework.security.autoconfig;

import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.core.security.userdetails.AppUserDetailService;
import io.github.toquery.framework.security.DelegatingSysUserOnline;
import io.github.toquery.framework.security.config.AppSecurityConfig;
import io.github.toquery.framework.security.exception.AppSecurityExceptionAdvice;
import io.github.toquery.framework.security.ignoring.AppSecurityJwtIgnoring;
import io.github.toquery.framework.security.properties.AppSecurityAdminProperties;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import io.github.toquery.framework.security.provider.JwtTokenProvider;
import io.github.toquery.framework.security.rest.AuthenticationRest;
import io.github.toquery.framework.security.rest.UserRegisterRest;
import io.github.toquery.framework.system.autoconfig.AppSystemAutoConfiguration;
import io.github.toquery.framework.system.service.ISysUserOnlineService;
import io.github.toquery.framework.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

/**
 * <a href="https://juejin.cn/post/6985893815500406791">https://juejin.cn/post/6985893815500406791</a>
 *
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@Import({AppSecurityConfig.class, AppSecurityJwtIgnoring.class})
@EnableConfigurationProperties({AppSecurityProperties.class, AppSecurityAdminProperties.class})
@AutoConfigureAfter({AppSystemAutoConfiguration.class})
public class AppSecurityAutoConfiguration {

    public AppSecurityAutoConfiguration() {
        log.info("自动装配 App Security 自动化配置");
    }


    @Bean
    @ConditionalOnMissingBean
    public AppSecurityExceptionAdvice appSecurityJwtExceptionAdvice() {
        return new AppSecurityExceptionAdvice();
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
    @ConditionalOnProperty(prefix = AppSecurityProperties.PREFIX, name = "register", havingValue = "true")
    public UserRegisterRest userRegisterRest(PasswordEncoder passwordEncoder, ISysUserService sysUserService) {
        log.debug("系统当前配置开启用户注册");
        return new UserRegisterRest(passwordEncoder, sysUserService);
    }


    @Bean
    @ConditionalOnMissingBean
    public JwtTokenProvider jwtTokenProvider(JwtEncoder encoder, AppSecurityAdminProperties appSecurityJwtProperties) {
        return new JwtTokenProvider(encoder, appSecurityJwtProperties);
    }


    @Bean
    @ConditionalOnMissingBean
    public DelegatingSysUserOnline delegatingSysUserOnline(JwtTokenProvider jwtTokenProvider,
                                                           ISysUserOnlineService sysUserOnlineService,
                                                           AppSecurityAdminProperties appSecurityJwtProperties) {
        return new DelegatingSysUserOnline(jwtTokenProvider, sysUserOnlineService, appSecurityJwtProperties);
    }

}
