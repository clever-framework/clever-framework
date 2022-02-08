package io.github.toquery.framework.front.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

/**
 * @author toquery
 * @version 1
 */
@Data
@ConfigurationProperties(prefix = AppFrontProperties.PREFIX)
public class AppFrontProperties {

    public static final String PREFIX = "app.front";

    private boolean enabled = true;

    private int timeToLiveInDays = 1461;

    private String prefix = "target" + File.separator;

    private String outputDir = "target" + File.separator + "classes" + File.separator + "static" + File.separator;
}
