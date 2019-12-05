package io.github.toquery.framework.webmvc.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Setter
@Getter
@Slf4j
//@Configuration
@ConfigurationProperties(prefix = AppWebMvcProperties.PREFIX)
public class AppWebMvcProperties {

    public AppWebMvcProperties() {
        log.info("AppWebMvcProperties");
    }

    public static final String PREFIX = "app.web-mvc";

    private AppWebParamProperties param = new AppWebParamProperties();

    private AppWebParamDefaultProperties defaultValue = new AppWebParamDefaultProperties();

}
