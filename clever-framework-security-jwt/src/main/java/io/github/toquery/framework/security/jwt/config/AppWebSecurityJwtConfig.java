package io.github.toquery.framework.security.jwt.config;

import io.github.toquery.framework.core.security.AppSecurityConfigurer;
import io.github.toquery.framework.core.security.AppSecurityIgnoring;
import io.github.toquery.framework.security.jwt.JwtUnauthorizedEntryPoint;
import io.github.toquery.framework.security.jwt.filter.JwtTokenAuthorizationFilter;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;

@Order(50)
@Slf4j
@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppWebSecurityJwtConfig implements AppSecurityConfigurer, AppSecurityIgnoring {

    public AppWebSecurityJwtConfig() {
        log.info("初始化 App Web Security Jwt 配置");
    }

    @Resource
    private JwtUnauthorizedEntryPoint unauthorizedEntryPoint;

    @Resource
    private AppSecurityJwtProperties appSecurityJwtProperties;

    @Bean
    public OncePerRequestFilter getFilter() {
        return new JwtTokenAuthorizationFilter();
    }


    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()

        // 2019-12-26 Security 调用链，顺序错误
        // jwt
        // .antMatchers(getCustomizeWhitelist()).permitAll() // Can't configure antMatchers after anyRequest
        // .anyRequest().authenticated() // Can't configure anyRequest after itself
        ;

        httpSecurity.addFilterBefore(getFilter(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(ignoring());
    }

    @Override
    public String[] ignoring() {
        AppSecurityJwtProperties.AppJwtPathProperties pathProperties = appSecurityJwtProperties.getPath();
        return new String[]{pathProperties.getRegister(), pathProperties.getToken()};
    }

}
