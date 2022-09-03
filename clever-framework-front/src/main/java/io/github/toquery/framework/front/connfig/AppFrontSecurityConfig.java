package io.github.toquery.framework.front.connfig;

import com.google.common.collect.Sets;
import io.github.toquery.framework.core.security.AppSecurityIgnoring;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class AppFrontSecurityConfig implements AppSecurityIgnoring {

    public AppFrontSecurityConfig() {
        log.info("AppFrontSecurityConfig");
    }

    public Set<String> ignoring() {
        return Sets.newHashSet("/favicon.ico", "/manifest.webapp", "/index.html", "/static/**", "/app/front/**");
    }

}
