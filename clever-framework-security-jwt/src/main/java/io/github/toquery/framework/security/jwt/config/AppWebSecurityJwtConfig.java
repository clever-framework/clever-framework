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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;

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

    @Resource
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoderBean());
    }

    @Bean
    public OncePerRequestFilter getFilter() {
        return new JwtAuthorizationTokenFilter(userDetailsService, jwtTokenUtil, appSecurityProperties, appSecurityJwtProperties);
    }

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
        //BCryptPasswordEncoder
//        return new PasswordEncoder() {
//            @Override
//            public String encode(CharSequence rawPassword) {
//                return "admin";
//            }
//
//            @Override
//            public boolean matches(CharSequence rawPassword, String encodedPassword) {
//                return true;
//            }
//        };
    }


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

    @Override
    protected String[] getCustomizeWhitelist() {
        AppSecurityJwtProperties.AppJwtPathProperties pathProperties = appSecurityJwtProperties.getPath();
        return new String[]{pathProperties.getRegister(), pathProperties.getToken()};
    }

   /*
   @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);

        AppSecurityJwtProperties.AppJwtPathProperties pathProperties = appSecurityJwtProperties.getPath();
        // AuthenticationTokenFilter will ignore the below paths
        // web.ignoring().antMatchers(HttpMethod.POST, pathProperties.getRegister(), pathProperties.getToken());
    }*/
}
