package io.github.toquery.framework.security.config;

import com.google.common.collect.Sets;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.core.security.AppSecurityConfigurer;
import io.github.toquery.framework.core.security.AppSecurityIgnoring;
import io.github.toquery.framework.security.handler.AppAccessDeniedHandler;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(51)
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private List<AppSecurityConfigurer> serviceSecConfigs;

    @Resource
    private List<AppSecurityIgnoring> appSecurityIgnoringList;

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

                //
                .antMatchers(this.getIgnoringArray()).permitAll()
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
        web.ignoring().antMatchers(this.getIgnoringArray());


        if (serviceSecConfigs != null && !serviceSecConfigs.isEmpty()) {
            for (AppSecurityConfigurer serviceSecConfig : serviceSecConfigs) {
                serviceSecConfig.configure(web);
            }
        }
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

    /**
     * 加载自定义白名单
     */
    protected Filter getCustomizeFilter() {
        return null;
    }
}
