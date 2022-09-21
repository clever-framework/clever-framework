package io.github.toquery.framework.jpa.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 */
@Setter
@Getter
//@Configuration
@ConfigurationProperties(prefix = AppDaoProperties.PREFIX)
public class AppDaoProperties {

    public static final String PREFIX = "app.dao";

}
