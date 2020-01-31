package io.github.toquery.framework.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

public interface AppWebSecurityConfigurer {

    void configure(HttpSecurity http) throws Exception;


    void configure(WebSecurity web);

}
