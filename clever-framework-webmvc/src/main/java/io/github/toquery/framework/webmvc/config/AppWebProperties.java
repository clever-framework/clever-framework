package io.github.toquery.framework.webmvc.config;

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
@ConfigurationProperties(prefix = "app.web")
public class AppWebProperties {

    private AppWebParamProperties param = new AppWebParamProperties();

    private AppWebParamDefaultProperties defaultValue = new AppWebParamDefaultProperties();

}
