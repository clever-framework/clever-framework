package io.github.toquery.framework.webmvc.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = AppWebMvcProperties.PREFIX)
public class AppWebMvcProperties {

    public static final String PREFIX = "app.web-mvc";

    private AppWebParamProperties param = new AppWebParamProperties();

    private AppWebParamDefaultProperties defaultValue = new AppWebParamDefaultProperties();

}
