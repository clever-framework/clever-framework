package io.github.toquery.framework.oauth2.resource.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 *
 */
@Data
@ConfigurationProperties(prefix = "app.security.oauth2.resourceserver")
public class AppOAuth2ResourceServerMultipleProperties {
    private Map<String, AppOAuth2ResourceServerProperties> multiple;
}
