package io.github.toquery.framework.security.jwt.config;

import io.github.toquery.framework.security.config.AppWebSecurityConfig;
import io.github.toquery.framework.security.jwt.JwtAuthenticationEntryPoint;
import io.github.toquery.framework.security.jwt.JwtTokenUtil;
import io.github.toquery.framework.security.jwt.filter.JwtAuthorizationTokenFilter;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;

@Order(50)
@Slf4j
@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppWebSecurityJwtConfig extends AppWebSecurityConfig {

    public AppWebSecurityJwtConfig() {
        log.info("初始化 App Web Security Jwt 配置");
    }

    public AppWebSecurityJwtConfig(boolean disableDefaults) {
        super(disableDefaults);
        log.info("初始化 App Web Security Jwt 配置，disableDefaults = {} ", disableDefaults);
    }

    @Resource
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private AppSecurityJwtProperties appSecurityJwtProperties;

    @Resource
    private AppSecurityProperties appSecurityProperties;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Bean
    public OncePerRequestFilter getFilter() {
        return new JwtAuthorizationTokenFilter(userDetailsService, jwtTokenUtil, appSecurityProperties, appSecurityJwtProperties);
    }


    /*
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        super.configure(httpSecurity);

        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()

                // jwt
                // .antMatchers(pathProperties.getRegister(), pathProperties.getToken()).permitAll()
                .anyRequest().authenticated()
        ;

        httpSecurity.addFilterBefore(getFilter(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl();
    }
    */

    @Override
    protected String[] getCustomizeWhitelist() {
        AppSecurityJwtProperties.AppJwtPathProperties pathProperties = appSecurityJwtProperties.getPath();
        return new String[]{pathProperties.getRegister(), pathProperties.getToken()};
    }

    @Override
    protected Filter getCustomizeFilter() {
        return getFilter();
    }
}
