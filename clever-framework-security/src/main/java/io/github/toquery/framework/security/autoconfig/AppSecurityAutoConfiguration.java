package io.github.toquery.framework.security.autoconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toquery.framework.core.security.AppSecurityIgnoring;
import io.github.toquery.framework.security.AppSecurityContextRepository;
import io.github.toquery.framework.security.endpoints.AppAccessDeniedHandler;
import io.github.toquery.framework.security.endpoints.AppAuthenticationEntryPoint;
import io.github.toquery.framework.security.endpoints.AppAuthenticationFailureHandler;
import io.github.toquery.framework.security.endpoints.AppAuthenticationSuccessHandler;
import io.github.toquery.framework.security.endpoints.AppLogoutSuccessHandler;
import io.github.toquery.framework.security.exception.AppSecurityExceptionAdvice;
import io.github.toquery.framework.security.ignoring.AppSecurityJwtIgnoring;
import io.github.toquery.framework.security.ignoring.DelegatingAppSecurityJwtIgnoring;
import io.github.toquery.framework.security.properties.AppSecurityAdminProperties;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.time.Duration;
import java.util.List;

/**
 * <a href="https://juejin.cn/post/6985893815500406791">https://juejin.cn/post/6985893815500406791</a>
 * 默认
 *
 * @author toquery
 * @version 1
 */
@Slf4j
@Import({AppSecurityJwtIgnoring.class})
@EnableConfigurationProperties({AppSecurityProperties.class, AppSecurityAdminProperties.class})
public class AppSecurityAutoConfiguration {


    public AppSecurityAutoConfiguration() {
        log.info("自动装配 App Security 自动化配置");
    }

    @Bean
    @ConditionalOnMissingBean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper objectMapper) {
        return new AppAccessDeniedHandler(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public LogoutSuccessHandler logoutSuccessHandler(ObjectMapper objectMapper) {
        return new AppLogoutSuccessHandler(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationEntryPoint authenticationEntryPoint(ObjectMapper objectMapper) {
        return new AppAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public AppAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AppAuthenticationSuccessHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public AppAuthenticationFailureHandler authenticationFailureHandler() {
        return new AppAuthenticationFailureHandler();
    }


    @Bean
    @ConditionalOnMissingBean
    public AppSecurityExceptionAdvice appSecurityJwtExceptionAdvice() {
        return new AppSecurityExceptionAdvice();
    }


    /**
     * 从request请求中那个地方获取到token
     */
    @Bean
    @ConditionalOnMissingBean
    public BearerTokenResolver bearerTokenResolver() {
        DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
        // 是否可以从uri请求参数中获取token
        bearerTokenResolver.setAllowUriQueryParameter(true);
        bearerTokenResolver.setAllowFormEncodedBodyParameter(true);
        return bearerTokenResolver;
    }

    /**
     * 从 JWT 的 scope 中获取的权限 取消 SCOPE_ 的前缀
     * 设置从 jwt claim 中那个字段获取权限
     * 如果需要同多个字段中获取权限或者是通过url请求获取的权限，则需要自己提供jwtAuthenticationConverter()这个方法的实现
     *
     * @return JwtAuthenticationConverter
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 去掉 SCOPE_ 的前缀
        // authoritiesConverter.setAuthorityPrefix("");
        // 从jwt claim 中那个字段获取权限，模式是从 scope 或 scp 字段中获取
        authoritiesConverter.setAuthoritiesClaimName("scope");
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }


    @Bean
    @ConditionalOnMissingBean
    public CorsConfiguration getCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*"); // SpringBoot2.4.0 [allowedOriginPatterns]代替[allowedOrigins]  .allowedOrigins("*")
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(Duration.ofDays(7));
        return corsConfiguration;
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityContextRepository securityContextRepository() {
        return new AppSecurityContextRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public DelegatingAppSecurityJwtIgnoring delegatingAppSecurityJwtIgnoring(
            AppSecurityProperties appSecurityProperties,
            List<AppSecurityIgnoring> appSecurityIgnoringList
    ) {
        return new DelegatingAppSecurityJwtIgnoring(appSecurityProperties, appSecurityIgnoringList);
    }

    /**
     * 自定义认证过滤器
     *
     * @param http http
     * @throws Exception 异常
     */
    @Bean
    @ConditionalOnProperty(prefix = AppSecurityProperties.PREFIX, value = "enabled-dsfc", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity http,
            JwtDecoder jwtDecoder,
            CorsConfiguration getCorsConfiguration,
            BearerTokenResolver bearerTokenResolver,
            AccessDeniedHandler accessDeniedHandler,
            LogoutSuccessHandler logoutSuccessHandler,
            AuthenticationEntryPoint authenticationEntryPoint,
            SecurityContextRepository securityContextRepository,
            JwtAuthenticationConverter jwtAuthenticationConverter
    ) throws Exception {
        log.debug("初始化默认认证过滤器");

        // 记录访问信息
        http.securityContext(httpSecuritySecurityContextConfigurer -> {
            httpSecuritySecurityContextConfigurer.requireExplicitSave(true);
            httpSecuritySecurityContextConfigurer.securityContextRepository(securityContextRepository);
        });


        // headers
        http.headers(headersConfigurer -> {
            headersConfigurer.disable();
            headersConfigurer.cacheControl().disable();
            headersConfigurer.frameOptions().sameOrigin().disable();
        });

        // cors
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(configurationSource -> getCorsConfiguration);
        });

        // csrf 忽略
        http.csrf(AbstractHttpConfigurer::disable);
        // 禁止form登录
        http.formLogin(AbstractHttpConfigurer::disable);
        // http Basic 认证处理
        http.httpBasic(HttpBasicConfigurer::disable);
        // x509 认证
        http.x509(AbstractHttpConfigurer::disable);
        // 匿名认证
        http.anonymous(anonymousConfigurer -> {
        });

        // session 管理， 关闭session
        http.sessionManagement(sessionManagementConfigurer -> {
            sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        // 授权http请求
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            // 允许所有 OPTIONS 请求
            authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.OPTIONS).permitAll();
            authorizationManagerRequestMatcherRegistry.requestMatchers("/", "/error").permitAll();
            authorizationManagerRequestMatcherRegistry.requestMatchers("/actuator/*").permitAll();

            // 获取框架配置和配置文件中的路径，并忽略认证
            authorizationManagerRequestMatcherRegistry.requestMatchers("/admin/login").permitAll();
            // authorizationManagerRequestMatcherRegistry.antMatchers(delegatingAppSecurityJwtIgnoring.getIgnoringArray()).permitAll();
            authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
        });


        // oauth2 资源服务配置
        http.oauth2ResourceServer(auth2ResourceServerConfigurer -> {
            // 处理 bearerToken方式，允许接受header、param、body的参数
            auth2ResourceServerConfigurer.bearerTokenResolver(bearerTokenResolver);
            auth2ResourceServerConfigurer.accessDeniedHandler(accessDeniedHandler);
            // 处理认证失败、过期
            auth2ResourceServerConfigurer.authenticationEntryPoint(authenticationEntryPoint);
            auth2ResourceServerConfigurer.jwt(jwtConfigurer -> {
                jwtConfigurer.decoder(jwtDecoder);
                jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter);

            });
        });

        // 异常处理
        http.exceptionHandling(exceptionHandlingConfigurer -> {
            exceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler);
            exceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint);
        });

        // 退出登录
        http.logout(logoutConfigurer -> {
            logoutConfigurer.logoutSuccessHandler(logoutSuccessHandler);
        });
        return http.build();
    }


}
