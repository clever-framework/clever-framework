package io.github.toquery.framework.security.jwt.autoconfig;

import io.github.toquery.framework.core.autoconfig.AppCoreAutoConfiguration;
import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.core.security.userdetails.AppUserDetailService;
import io.github.toquery.framework.security.autoconfig.AppSecurityAutoConfiguration;
import io.github.toquery.framework.security.jwt.handler.JwtTokenHandler;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.security.jwt.rest.JwtAuthenticationRest;
import io.github.toquery.framework.system.autoconfig.AppSystemAutoConfiguration;
import io.github.toquery.framework.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@AutoConfigureAfter({AppCoreAutoConfiguration.class, AppSystemAutoConfiguration.class, AppSecurityAutoConfiguration.class})
@EnableConfigurationProperties(AppSecurityJwtProperties.class)
//@ComponentScan(basePackages = "io.github.toquery.framework.security.jwt")
@ConditionalOnProperty(prefix = AppSecurityJwtProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class AppSecurityJwtAutoConfiguration {

//    @Autowired
//    private AppProperties appProperties;

    public AppSecurityJwtAutoConfiguration() {
        log.info("自动装配 App Security Jwt 自动化配置");
    }


    @Bean
    @ConditionalOnMissingBean
    public JwtTokenHandler getJwtTokenUtil() {
        return new JwtTokenHandler();
    }

    @Bean
    @DependsOn("appProperties")
    @ConditionalOnMissingBean
    public JwtAuthenticationRest getJwtAuthenticationRest(AppSecurityJwtProperties appSecurityJwtProperties, AuthenticationManager authenticationManager, JwtTokenHandler jwtTokenHandler, ISysUserService sysUserService, AppProperties appProperties, AppUserDetailService appUserDetailsService, PasswordEncoder passwordEncoder) {
        return new JwtAuthenticationRest(appSecurityJwtProperties, authenticationManager, jwtTokenHandler, sysUserService, appProperties, appUserDetailsService, passwordEncoder);
    }
}
