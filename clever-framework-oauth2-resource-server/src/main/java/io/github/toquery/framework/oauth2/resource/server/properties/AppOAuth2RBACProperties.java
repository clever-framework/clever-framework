package io.github.toquery.framework.oauth2.resource.server.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 */
@Slf4j
@Setter
@Getter
@ConfigurationProperties(prefix = "app.security.oauth2.rbac")
public class AppOAuth2RBACProperties {
    private String clientId;

    private String domain;

    private String uri;
}
