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
import io.github.toquery.framework.security.AppAuthenticationFailureEntryPoint;
import io.github.toquery.framework.security.DelegatingSysUserOnline;
import io.github.toquery.framework.security.auditor.AppAuditorAwareImpl;
import io.github.toquery.framework.security.config.AppSecurityJwtIgnoring;
import io.github.toquery.framework.security.exception.AppSecurityExceptionAdvice;
import io.github.toquery.framework.security.handler.AppAccessDeniedHandler;
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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import javax.annotation.Resource;
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
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableConfigurationProperties({AppSecurityProperties.class, AppSecurityJwtProperties.class})
@AutoConfigureAfter({AppSystemAutoConfiguration.class})
// @ComponentScan(basePackages = "io.github.toquery.framework.security")
//@ConditionalOnBean(value = {AppSystemAutoConfiguration.class, ISysUserService.class})
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
    public AuditorAware<Long> auditorProvider() {
        return new AppAuditorAwareImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 从request请求中那个地方获取到token
     */
    private BearerTokenResolver bearerTokenResolver() {
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
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 去掉 SCOPE_ 的前缀
        authoritiesConverter.setAuthorityPrefix("");
        // 从jwt claim 中那个字段获取权限，模式是从 scope 或 scp 字段中获取
        authoritiesConverter.setAuthoritiesClaimName("scope");
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    /**
     * jwt 的解码器
     *
     * @return JwtDecoder

    public JwtDecoder jwtDecoder() {
    // 授权服务器 jwk 的信息
    NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri("http://qq.com:8080/oauth2/jwks")
    // 设置获取 jwk 信息的超时时间
    .restOperations(
    builder.setReadTimeout(Duration.ofSeconds(3))
    .setConnectTimeout(Duration.ofSeconds(3))
    .build()
    )
    .build();
    // 对jwt进行校验
    decoder.setJwtValidator(JwtValidators.createDefault());
    // 对 jwt 的 claim 中增加值
    decoder.setClaimSetConverter(
    MappedJwtClaimSetConverter.withDefaults(Collections.singletonMap("为claim中增加key", custom -> "值"))
    );
    return decoder;
    }
     */

    /**
     * 自定义认证过滤器
     *
     * @param http http
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AppAuthenticationFailureEntryPoint appAuthenticationFailureEntryPoint = new AppAuthenticationFailureEntryPoint();
        http
                // 授权http请求
                .authorizeHttpRequests((authorize) -> authorize
                        // 获取框架配置和配置文件中的路径，并忽略认证
                        .antMatchers(this.getIgnoringArray()).permitAll()
                        .anyRequest().authenticated()
                )
                // csrf 忽略
                .csrf((csrf) -> csrf.ignoringAntMatchers("/user/login"))

                // http Basic 认证处理
                .httpBasic(httpBasicCustomizer -> {
                    // 使用委派类
//                    LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> entryPoints = new LinkedHashMap<>();
//                    entryPoints.put(new AntPathRequestMatcher("/user/login"), appAuthenticationFailureEntryPoint);
//                    DelegatingAuthenticationEntryPoint delegatingAuthenticationEntryPoint = new DelegatingAuthenticationEntryPoint(entryPoints);

                    httpBasicCustomizer.authenticationEntryPoint(appAuthenticationFailureEntryPoint);
                })

                .oauth2ResourceServer((oauth2ResourceServerCustomizer) -> {
                    oauth2ResourceServerCustomizer
                            .bearerTokenResolver(bearerTokenResolver())
                            .jwt()
                            // .decoder(jwtDecoder())
                            .jwtAuthenticationConverter(jwtAuthenticationConverter());
                })

                // session 管理， 关闭session
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 异常处理
                .exceptionHandling((exceptions) -> exceptions
                                .accessDeniedHandler(new AppAccessDeniedHandler())
                                .authenticationEntryPoint(new AppAuthenticationFailureEntryPoint())
//                                .authenticationEntryPoint(new JwtUnauthorizedEntryPoint())
//                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
//                        .defaultAuthenticationEntryPointFor(new AppAuthenticationFailureEntryPoint(), new AntPathRequestMatcher("/**"))
//                        .defaultAccessDeniedHandlerFor(new AppAccessDeniedHandler(), new AntPathRequestMatcher("/**"))
                )

                // TODO 这个配置是否需要？
                .headers(headersCustomizer -> headersCustomizer.frameOptions().sameOrigin().cacheControl())

        ;
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
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(appSecurityJwtProperties.getKey().getPublicKey())
                .jwtProcessorCustomizer(jwtProcessorCustomizer -> {
                    JwtValidators.createDefault();
                })
                .build();
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
                                                          DelegatingSysUserOnline sysUserOnlineHandler,
                                                          AppSecurityJwtProperties appSecurityJwtProperties) {
        return new JwtAuthenticationRest(jwtEncoder, appProperties, passwordEncoder, sysUserService,
                appUserDetailsService, sysUserOnlineHandler, appSecurityJwtProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public DelegatingSysUserOnline delegatingSysUserOnline(JwtEncoder encoder,
                                                           ISysUserOnlineService sysUserOnlineService,
                                                           AppSecurityJwtProperties appSecurityJwtProperties) {
        return new DelegatingSysUserOnline(encoder, sysUserOnlineService, appSecurityJwtProperties);
    }

}
