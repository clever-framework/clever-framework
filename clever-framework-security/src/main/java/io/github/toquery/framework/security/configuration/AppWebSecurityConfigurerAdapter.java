package io.github.toquery.framework.security.configuration;

import io.github.toquery.framework.security.web.builders.AppWebSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

public class AppWebSecurityConfigurerAdapter implements WebSecurityConfigurer<AppWebSecurity> {
    @Override
    public void init(AppWebSecurity builder) throws Exception {

    }

    @Override
    public void configure(AppWebSecurity builder) throws Exception {

    }
}
