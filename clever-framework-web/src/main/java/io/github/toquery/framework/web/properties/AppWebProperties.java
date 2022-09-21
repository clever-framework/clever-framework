package io.github.toquery.framework.web.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author toquery
 * @version 1
 */

@Setter
@Getter
@ConfigurationProperties(prefix = AppWebProperties.PREFIX)
public class AppWebProperties {
    public static final String PREFIX = "app.web";

    private boolean enabled = true;
}
