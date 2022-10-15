package io.github.toquery.framework.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.toquery.framework.core.security.AppSecurityIgnoring;
import io.github.toquery.framework.security.AppSecurityContextRepository;
import io.github.toquery.framework.security.endpoints.AppAccessDeniedHandler;
import io.github.toquery.framework.security.endpoints.AppAuthenticationEntryPoint;
import io.github.toquery.framework.security.endpoints.AppLogoutSuccessHandler;
import io.github.toquery.framework.security.ignoring.DelegatingAppSecurityJwtIgnoring;
import io.github.toquery.framework.security.properties.AppSecurityAdminProperties;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;

/**
 *
 */
public class AppSecurityConfig {


    @Resource
    private AppSecurityAdminProperties appSecurityJwtProperties;

    @Bean
    @ConditionalOnMissingBean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper objectMapper) {
        return new AppAccessDeniedHandler(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new AppLogoutSuccessHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationEntryPoint authenticationEntryPoint(ObjectMapper objectMapper) {
        return new AppAuthenticationEntryPoint(objectMapper);
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
    public PasswordEncoder passwordEncoder() {
        // return new BCryptPasswordEncoder();
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
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
        authoritiesConverter.setAuthorityPrefix("");
        // 从jwt claim 中那个字段获取权限，模式是从 scope 或 scp 字段中获取
        authoritiesConverter.setAuthoritiesClaimName("scope");
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityContextRepository securityContextRepository() {
        return new AppSecurityContextRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public DelegatingAppSecurityJwtIgnoring delegatingAppSecurityJwtIgnoring(AppSecurityProperties appSecurityProperties,
                                                                             List<AppSecurityIgnoring> appSecurityIgnoringList) {
        return new DelegatingAppSecurityJwtIgnoring(appSecurityProperties, appSecurityIgnoringList);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(appSecurityJwtProperties.getKey().getPublicKey())
                .jwtProcessorCustomizer(jwtProcessorCustomizer -> {
                    JwtValidators.createDefaultWithIssuer(appSecurityJwtProperties.getIssuer());
                })
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtEncoder jwtEncoder() {
        AppSecurityAdminProperties.AppSecurityAdminJwtKey appJwtKey = appSecurityJwtProperties.getKey();
        JWK jwk = new RSAKey.Builder(appJwtKey.getPublicKey()).privateKey(appJwtKey.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public AppAuthenticationSuccessHandler authenticationSuccessHandler() {
//        return new AppAuthenticationSuccessHandler();
//    }
//    @Bean
//    @ConditionalOnMissingBean
//    public AppAuthenticationFailureHandler authenticationFailureHandler() {
//        return new AppAuthenticationFailureHandler();
//    }


    /**
     * 自定义认证过滤器
     *
     * @param http http
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtDecoder jwtDecoder,
                                                   CorsConfiguration getCorsConfiguration,
                                                   BearerTokenResolver bearerTokenResolver,
                                                   AccessDeniedHandler accessDeniedHandler,
                                                   LogoutSuccessHandler logoutSuccessHandler,
                                                   AuthenticationEntryPoint authenticationEntryPoint,
                                                   SecurityContextRepository securityContextRepository,
                                                   JwtAuthenticationConverter jwtAuthenticationConverter,
                                                   DelegatingAppSecurityJwtIgnoring delegatingAppSecurityJwtIgnoring) throws Exception {
        // csrfConfigurer.ignoringAntMatchers("/user/login");

        // 记录访问信息
        http.securityContext(httpSecuritySecurityContextConfigurer -> {
            httpSecuritySecurityContextConfigurer.requireExplicitSave(true);
            httpSecuritySecurityContextConfigurer.securityContextRepository(securityContextRepository);
        });

        // 认证管理器
        // http.authenticationManager(authenticationManager);

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

        // session 管理， 关闭session
        http.sessionManagement(sessionManagementConfigurer -> {
            sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        // 授权http请求
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            // 允许所有 OPTIONS 请求
            authorizationManagerRequestMatcherRegistry.antMatchers(HttpMethod.OPTIONS).permitAll();
//            authorizationManagerRequestMatcherRegistry.antMatchers("/admin/**").hasAuthority("ADMIN");
//            authorizationManagerRequestMatcherRegistry.antMatchers("/app/**").hasAuthority("APP");
            authorizationManagerRequestMatcherRegistry.antMatchers("/error").permitAll();
            authorizationManagerRequestMatcherRegistry.mvcMatchers("/error").permitAll();

            // 获取框架配置和配置文件中的路径，并忽略认证
            authorizationManagerRequestMatcherRegistry.antMatchers("/admin/login").permitAll();
            // authorizationManagerRequestMatcherRegistry.antMatchers(delegatingAppSecurityJwtIgnoring.getIgnoringArray()).permitAll();
            authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
        });

        // 禁止form登录
        http.formLogin(AbstractHttpConfigurer::disable);
        // http Basic 认证处理
        http.httpBasic(HttpBasicConfigurer::disable);
        // x509 认证
        http.x509(AbstractHttpConfigurer::disable);
        // 匿名认证
        http.anonymous(anonymousConfigurer -> {
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
                // jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter);
                jwtConfigurer.authenticationManager(new ProviderManager(new JwtAuthenticationProvider(jwtDecoder)));

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
