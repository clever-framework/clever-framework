package io.github.toquery.framework.front.connfig;

import io.github.toquery.framework.security.config.AppWebSecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Slf4j
@Order(52)
public class AppFrontSecurityConfig extends AppWebSecurityConfig {

    public AppFrontSecurityConfig() {
        log.info("AppFrontSecurityConfig");
    }

    @Override
    protected String[] getCustomizeWhitelist() {
        return new String[]{"/manifest.webapp", "/index.html", "/static/**"};
    }
}
