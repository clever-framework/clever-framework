package io.github.toquery.framework.security.config;

import io.github.toquery.framework.security.properties.AppSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;

@Order(51)
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AppSecurityProperties appSecurityProperties;

    public AppWebSecurityConfig() {
        log.info("初始化 App Web Security 配置");
    }

    public AppWebSecurityConfig(boolean disableDefaults) {
        super(disableDefaults);
        log.info("初始化 App Web Security 配置，disableDefaults = {} ", disableDefaults);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                // .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()


                .authorizeRequests()

                // jwt
                .antMatchers(this.getWhitelist()).permitAll()
                .anyRequest().authenticated()
        ;

        // httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // AuthenticationTokenFilter will ignore the below paths
        web.ignoring().antMatchers(this.getWhitelist());
    }

    /**
     * 加载白名单
     */
    private String[] getWhitelist() {
        return ArrayUtils.addAll(appSecurityProperties.getWhitelistArray(), this.getCustomizeWhitelist());
    }

    /**
     * 加载自定义白名单
     */
    protected String[] getCustomizeWhitelist() {
        return new String[]{};
    }
}
