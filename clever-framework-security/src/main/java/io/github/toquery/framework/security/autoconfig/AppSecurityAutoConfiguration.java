package io.github.toquery.framework.security.autoconfig;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.core.security.userdetails.AppUserDetailService;
import io.github.toquery.framework.security.SysyUserOnlineJwtServiceImpl;
import io.github.toquery.framework.security.auditor.AppAuditorAwareImpl;
import io.github.toquery.framework.security.config.AppWebSecurityConfig;
import io.github.toquery.framework.security.config.AppWebSecurityJwtConfig;
import io.github.toquery.framework.security.exception.AppSecurityExceptionAdvice;
import io.github.toquery.framework.security.handler.AppAuthenticationFailureHandler;
import io.github.toquery.framework.security.handler.AppAuthenticationSuccessHandler;
import io.github.toquery.framework.security.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import io.github.toquery.framework.security.rest.JwtAuthenticationRest;
import io.github.toquery.framework.system.autoconfig.AppSystemAutoConfiguration;
import io.github.toquery.framework.system.service.ISysUserOnlineService;
import io.github.toquery.framework.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@Import({AppWebSecurityConfig.class, AppWebSecurityJwtConfig.class})
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableConfigurationProperties({AppSecurityProperties.class, AppSecurityJwtProperties.class})
@AutoConfigureAfter({AppSystemAutoConfiguration.class})
// @ComponentScan(basePackages = "io.github.toquery.framework.security")
//@ConditionalOnBean(value = {AppSystemAutoConfiguration.class, ISysUserService.class})
public class AppSecurityAutoConfiguration {

    @Resource
    private AppSecurityJwtProperties appSecurityJwtProperties;

    public AppSecurityAutoConfiguration() {
        log.info("自动装配 App Security 自动化配置");
    }

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new AppAuditorAwareImpl();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(appSecurityJwtProperties.getKey().getPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        AppSecurityJwtProperties.AppJwtKey appJwtKey = appSecurityJwtProperties.getKey();
        JWK jwk = new RSAKey.Builder(appJwtKey.getPublicKey()).privateKey(appJwtKey.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public AppSecurityExceptionAdvice appSecurityJwtExceptionAdvice() {
        return new AppSecurityExceptionAdvice();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
    AppAuthenticationSuccessHandler appAuthenticationSuccessHandler() {
        return new AppAuthenticationSuccessHandler();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    AppAuthenticationFailureHandler appAuthenticationFailureHandler() {
        return new AppAuthenticationFailureHandler();
    }


    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationRest getJwtAuthenticationRest(JwtEncoder jwtEncoder,
                                                          AppProperties appProperties,
                                                          PasswordEncoder passwordEncoder,
                                                          ISysUserService sysUserService,
                                                          AppUserDetailService appUserDetailsService,
                                                          ISysUserOnlineService sysUserOnlineService,
                                                          AppSecurityJwtProperties appSecurityJwtProperties) {
        return new JwtAuthenticationRest(jwtEncoder, appProperties, passwordEncoder, sysUserService,
                appUserDetailsService, sysUserOnlineService, appSecurityJwtProperties);
    }

    @Bean
//    @ConditionalOnMissingBean
    public ISysUserOnlineService sysUserOnlineService(JwtEncoder encoder,
                                                      AppSecurityJwtProperties appSecurityJwtProperties) {
        return new SysyUserOnlineJwtServiceImpl(encoder, appSecurityJwtProperties);
    }

}
