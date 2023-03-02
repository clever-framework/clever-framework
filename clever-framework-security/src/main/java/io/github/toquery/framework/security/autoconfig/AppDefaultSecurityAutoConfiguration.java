package io.github.toquery.framework.security.autoconfig;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.toquery.framework.security.properties.AppSecurityAdminProperties;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import io.github.toquery.framework.security.provider.JwtTokenProvider;
import io.github.toquery.framework.security.utils.AppRSAUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;

/**
 *
 */
@Slf4j
@ConditionalOnProperty(prefix = AppSecurityProperties.PREFIX, value = "enabled", havingValue = "true", matchIfMissing = true)
public class AppDefaultSecurityAutoConfiguration {

    @Resource
    private AppSecurityProperties appSecurityProperties;

    public AppDefaultSecurityAutoConfiguration() {
        log.debug("App 启用默认 Security 配置，包括 JwtEncoder、JwtDecoder、AuthenticationManager(defaultAuthenticationManager)、SecurityFilterChain(defaultSecurityFilterChain), JwtTokenProvider");
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtEncoder jwtEncoder() {
        AppSecurityProperties.AppSecurityJwtKey appSecurityAdminJwtKey = appSecurityProperties.getKey();
        JWK jwk = new RSAKey.Builder(AppRSAUtils.publicKey(appSecurityAdminJwtKey.getPublicKey()))
                .privateKey(AppRSAUtils.privateKey(appSecurityAdminJwtKey.getPrivateKey()))
                .keyID(appSecurityAdminJwtKey.getKeyId())
                .build();
        JWKSet jwkSet = new JWKSet(jwk);
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(jwkSet);
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(AppRSAUtils.publicKey(appSecurityProperties.getKey().getPublicKey()))
                .jwtProcessorCustomizer(jwtProcessorCustomizer -> {
                    JwtValidators.createDefaultWithIssuer(appSecurityProperties.getKey().getIssuer());
                })
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtTokenProvider jwtTokenProvider(
            JwtEncoder encoder,
            AppSecurityAdminProperties appSecurityJwtProperties
    ) {
        return new JwtTokenProvider(encoder, appSecurityJwtProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationManager defaultAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    /**
     * 自定义认证过滤器
     *
     * @param http http
     * @throws Exception 异常
     */
    @Bean
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
