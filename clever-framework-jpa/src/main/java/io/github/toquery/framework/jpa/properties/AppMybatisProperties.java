package io.github.toquery.framework.jpa.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 */
@Data
@ConfigurationProperties(prefix = AppMybatisProperties.PREFIX)
public class AppMybatisProperties {

    public static final String PREFIX = "app.mybatis";

    private boolean enabled = true;

}
