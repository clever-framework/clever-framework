package io.github.toquery.framework.security.autoconfig;

import com.google.common.collect.Sets;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.core.security.AppSecurityIgnoring;
import io.github.toquery.framework.core.security.userdetails.AppUserDetailService;
import io.github.toquery.framework.security.AppSecurityContextRepository;
import io.github.toquery.framework.security.DelegatingSysUserOnline;
import io.github.toquery.framework.security.auditor.AppAuditorAwareImpl;
import io.github.toquery.framework.security.endpoints.AppAccessDeniedHandler;
import io.github.toquery.framework.security.endpoints.AppAuthenticationFailureEntryPoint;
import io.github.toquery.framework.security.endpoints.AppAuthenticationFailureHandler;
import io.github.toquery.framework.security.endpoints.AppAuthenticationSuccessHandler;
import io.github.toquery.framework.security.exception.AppSecurityExceptionAdvice;
import io.github.toquery.framework.security.ignoring.AppSecurityJwtIgnoring;
import io.github.toquery.framework.security.ignoring.DelegatingAppSecurityJwtIgnoring;
import io.github.toquery.framework.security.properties.AppSecurityJwtProperties;
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
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
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
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <a href="https://juejin.cn/post/6985893815500406791">https://juejin.cn/post/6985893815500406791</a>
 *
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@Import({AppSecurityJwtIgnoring.class})
//@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableConfigurationProperties({AppSecurityProperties.class, AppSecurityJwtProperties.class})
@AutoConfigureAfter({AppSystemAutoConfiguration.class})
public class AppSecurityAutoConfiguration {

    @Resource
    private List<AppSecurityIgnoring> appSecurityIgnoringList;

    @Resource
    private AppSecurityProperties appSecurityProperties;

    @Resource
    private AppSecurityJwtProperties appSecurityJwtProperties;

    public AppSecurityAutoConfiguration() {
        log.info("自动装配 App Security 自动化配置");
    }

    @Bean
    @ConditionalOnMissingBean
    public AuditorAware<Long> auditorProvider() {
        return new AppAuditorAwareImpl();
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

    /**
     * 自定义认证过滤器
     *
     * @param http http
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtDecoder jwtDecoder,
                                                   BearerTokenResolver bearerTokenResolver,
                                                   AuthenticationManager authenticationManager,
                                                   SecurityContextRepository securityContextRepository,
                                                   JwtAuthenticationConverter jwtAuthenticationConverter,
                                                   DelegatingAppSecurityJwtIgnoring delegatingAppSecurityJwtIgnoring) throws Exception {
        // csrfConfigurer.ignoringAntMatchers("/user/login");
        http
                // 记录访问信息
                .securityContext(httpSecuritySecurityContextConfigurer -> {
                    httpSecuritySecurityContextConfigurer.requireExplicitSave(true);
                    httpSecuritySecurityContextConfigurer.securityContextRepository(securityContextRepository);
                })
                // 认证管理器
                .authenticationManager(authenticationManager)
                // headers
                .headers(headersConfigurer -> {
                    headersConfigurer.disable();
                    headersConfigurer.cacheControl().disable();
                    headersConfigurer.frameOptions().sameOrigin().disable();
                })

                // csrf 忽略
                .csrf(AbstractHttpConfigurer::disable)

                // cors
                .cors(httpSecurityCorsConfigurer -> {
                    httpSecurityCorsConfigurer.configurationSource(configurationSource -> {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.addAllowedOriginPattern("*");
                        corsConfiguration.addAllowedHeader("*");
                        corsConfiguration.addAllowedMethod("*");
                        corsConfiguration.setAllowCredentials(true);
                        corsConfiguration.setMaxAge(Duration.ofDays(7));
                        return corsConfiguration;
                    });
                })

                // session 管理， 关闭session
                .sessionManagement(sessionManagementConfigurer -> {
                    sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })

                // 授权http请求
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    // 允许所有 OPTIONS 请求
                    authorizationManagerRequestMatcherRegistry.antMatchers(HttpMethod.OPTIONS).permitAll();
                    authorizationManagerRequestMatcherRegistry.antMatchers("/error").permitAll();
                    authorizationManagerRequestMatcherRegistry.mvcMatchers("/error").permitAll();

                    // 获取框架配置和配置文件中的路径，并忽略认证
                    authorizationManagerRequestMatcherRegistry.antMatchers(delegatingAppSecurityJwtIgnoring.getIgnoringArray()).permitAll();
                    authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
                })

                // 禁止form登录
                .formLogin(AbstractHttpConfigurer::disable)

                // http Basic 认证处理
                .httpBasic(HttpBasicConfigurer::disable)
//                .httpBasic(httpSecurityHttpBasicConfigurer -> {
//                    httpSecurityHttpBasicConfigurer.realmName("clever-framework-security");
//                    httpSecurityHttpBasicConfigurer.authenticationEntryPoint(new AppHttpBasicAuthenticationFailureEntryPoint(appSecurityJwtProperties));
//                    httpSecurityHttpBasicConfigurer.authenticationDetailsSource(new AppAuthenticationDetailsSource());
//                })

                // oauth2 资源服务配置
                .oauth2ResourceServer(oAuth2ResourceServerConfigurer -> {
                    oAuth2ResourceServerConfigurer.bearerTokenResolver(bearerTokenResolver);
                    oAuth2ResourceServerConfigurer.jwt(jwtConfigurer -> {
                        jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter);
                        jwtConfigurer.decoder(jwtDecoder);
                    });
                })


                // 异常处理
                .exceptionHandling(exceptionHandlingConfigurer -> {
                    exceptionHandlingConfigurer.accessDeniedHandler(new AppAccessDeniedHandler());
                    exceptionHandlingConfigurer.authenticationEntryPoint(new AppAuthenticationFailureEntryPoint());
//                                .authenticationEntryPoint(new JwtUnauthorizedEntryPoint())
//                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
//                        .defaultAuthenticationEntryPointFor(new AppAuthenticationFailureEntryPoint(), new AntPathRequestMatcher("/**"))
//                        .defaultAccessDeniedHandlerFor(new AppAccessDeniedHandler(), new AntPathRequestMatcher("/**"))
                });
        return http.build();
    }

    /**
     * 加载白名单
     */
    private String[] getIgnoringArray() {
        Set<String> ignoringSet = Sets.newHashSet();
        // 获取框架配置的地址
        if (appSecurityIgnoringList != null && !appSecurityIgnoringList.isEmpty()) {
            ignoringSet.addAll(appSecurityIgnoringList.stream().flatMap(appSecurityIgnoring -> appSecurityIgnoring.ignoring().stream()).collect(Collectors.toSet()));
            log.debug("加载框架白名单URI {} 个 , 分别是 {}", ignoringSet.size(), JacksonUtils.object2String(ignoringSet));
        }
        ignoringSet.addAll(appSecurityProperties.getIgnoring());
        log.debug("加载配置文件白名单URI {} 个 , 分别是 {}", appSecurityProperties.getIgnoring().size(), JacksonUtils.object2String(appSecurityProperties.getIgnoring()));
        String[] ignoringArray = new String[ignoringSet.size()];
        return ignoringSet.toArray(ignoringArray);
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
        AppSecurityJwtProperties.AppSecurityJwtKey appJwtKey = appSecurityJwtProperties.getKey();
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
    public AppAuthenticationSuccessHandler appAuthenticationSuccessHandler() {
        return new AppAuthenticationSuccessHandler();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    public AppAuthenticationFailureHandler appAuthenticationFailureHandler() {
        return new AppAuthenticationFailureHandler();
    }


    @Bean
    @ConditionalOnMissingBean
    public AuthenticationRest authenticationRest(AppProperties appProperties,
                                                 ISysUserService sysUserService,
                                                 AppUserDetailService appUserDetailsService,
                                                 DelegatingSysUserOnline sysUserOnlineHandler,
                                                 AuthenticationManager authenticationManager) {
        return new AuthenticationRest(appProperties, appUserDetailsService, sysUserOnlineHandler, sysUserService, authenticationManager);
    }

    @Bean
    @ConditionalOnProperty(prefix = AppSecurityProperties.PREFIX, name = "register", havingValue = "true")
    public UserRegisterRest userRegisterRest(PasswordEncoder passwordEncoder, ISysUserService sysUserService) {
        log.debug("系统当前配置开启用户注册");
        return new UserRegisterRest(passwordEncoder, sysUserService);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtTokenProvider jwtTokenProvider(JwtEncoder encoder, AppSecurityJwtProperties appSecurityJwtProperties) {
        return new JwtTokenProvider(encoder, appSecurityJwtProperties);
    }


    @Bean
    @ConditionalOnMissingBean
    public DelegatingSysUserOnline delegatingSysUserOnline(JwtTokenProvider jwtTokenProvider,
                                                           ISysUserOnlineService sysUserOnlineService,
                                                           AppSecurityJwtProperties appSecurityJwtProperties) {
        return new DelegatingSysUserOnline(jwtTokenProvider, sysUserOnlineService, appSecurityJwtProperties);
    }

}
