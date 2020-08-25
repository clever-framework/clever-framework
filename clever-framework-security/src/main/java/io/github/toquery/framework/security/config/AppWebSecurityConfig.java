package io.github.toquery.framework.security.config;

import io.github.toquery.framework.core.security.AppSecurityConfigurer;
import io.github.toquery.framework.security.handler.AppAccessDeniedHandler;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.List;

@Order(51)
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private List<AppSecurityConfigurer> serviceSecConfigs;

    @Resource
    private AppSecurityProperties appSecurityProperties;

    public AppWebSecurityConfig() {
        log.info("初始化 App Web Security 配置");
    }

    public AppWebSecurityConfig(boolean disableDefaults) {
        super(disableDefaults);
        log.info("初始化 App Web Security 配置，disableDefaults = {} ", disableDefaults);
    }

    // TODO 这里版本冲突，新版 Spring 后无法注入 UserDetailsService
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /*
    @Resource
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoderBean());
    }
    */


    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }


    /**
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                // .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .authorizeRequests()

                // jwt
                .antMatchers(this.getWhitelist()).permitAll()
                .anyRequest().authenticated();

        if (serviceSecConfigs != null && !serviceSecConfigs.isEmpty()) {
            for (AppSecurityConfigurer serviceSecConfig : serviceSecConfigs) {
                serviceSecConfig.configure(http);
            }
        }

        // 添加自定义异常入口
        http
                .exceptionHandling()
                .accessDeniedHandler(new AppAccessDeniedHandler())
        ;

        if (getCustomizeFilter() != null) {
            http.addFilterBefore(getCustomizeFilter(), UsernamePasswordAuthenticationFilter.class);
        }

        // disable page caching
        http.headers().frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl();
    }

    /**
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {

        // AuthenticationTokenFilter will ignore the below paths
        web.ignoring().antMatchers(this.getWhitelist());


        if (serviceSecConfigs != null && !serviceSecConfigs.isEmpty()) {
            for (AppSecurityConfigurer serviceSecConfig : serviceSecConfigs) {
                serviceSecConfig.configure(web);
            }
        }
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

    /**
     * 加载自定义白名单
     */
    protected Filter getCustomizeFilter() {
        return null;
    }
}
