package io.github.toquery.framework.system.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author toquery
 * @version 1
 */
@Data
@ConfigurationProperties(prefix = AppSystemProperties.PREFIX)
public class AppSystemProperties {

    public static final String PREFIX = "app.system";

    private boolean enable = true;

    private boolean generateMenu = true;

    private boolean generateView = true;

}
