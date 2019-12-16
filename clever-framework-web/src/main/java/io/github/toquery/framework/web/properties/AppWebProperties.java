package io.github.toquery.framework.web.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

/**
 * @author toquery
 * @version 1
 */

@Setter
@Getter
@ConfigurationProperties(prefix = AppWebProperties.PREFIX)
public class AppWebProperties {
    public static final String PREFIX = "app.web";

    private boolean enable = true;

    private CorsConfiguration cors = new CorsConfiguration();
}
