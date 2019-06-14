package io.github.toquery.framework.front.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @author toquery
 * @version 1
 */
@Data
@Configuration
@ConfigurationProperties(prefix = AppFrontProperties.PATH)
public class AppFrontProperties {

    public static final String PATH = "app.front";

    private String prefix = "target" + File.separator;

    private String outputDir = "target" + File.separator + "www" + File.separator;
}
