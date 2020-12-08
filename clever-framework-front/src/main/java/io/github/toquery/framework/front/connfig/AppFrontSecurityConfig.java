package io.github.toquery.framework.front.connfig;

import com.google.common.collect.Sets;
import io.github.toquery.framework.core.security.AppSecurityConfigurer;
import io.github.toquery.framework.core.security.AppSecurityIgnoring;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

import java.util.Set;

@Slf4j
public class AppFrontSecurityConfig implements AppSecurityConfigurer, AppSecurityIgnoring {

    public AppFrontSecurityConfig() {
        log.info("AppFrontSecurityConfig");
    }

    public Set<String> ignoring() {
        return Sets.newHashSet("/manifest.webapp", "/index.html", "/static/**", "/app/front/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

    }

    @Override
    public void configure(WebSecurity web) {
        // web.ignoring().antMatchers(ignoring());
    }
}
