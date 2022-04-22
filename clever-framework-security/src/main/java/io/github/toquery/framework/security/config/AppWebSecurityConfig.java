package io.github.toquery.framework.security.config;

import com.google.common.collect.Sets;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.core.security.AppSecurityIgnoring;
import io.github.toquery.framework.security.AppAuthenticationFailureEntryPoint;
import io.github.toquery.framework.security.handler.AppAccessDeniedHandler;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(51)
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppWebSecurityConfig {

    @Resource
    private List<AppSecurityIgnoring> appSecurityIgnoringList;

    @Resource
    private AppSecurityProperties appSecurityProperties;

    public AppWebSecurityConfig() {
        log.info("初始化 App Web Security 配置");
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 修改前缀
        authoritiesConverter.setAuthorityPrefix("");
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

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
                    oauth2ResourceServerCustomizer.jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
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

}
