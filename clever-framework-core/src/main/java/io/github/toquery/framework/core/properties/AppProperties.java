package io.github.toquery.framework.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 常量
 *
 * @author toquery
 * @version 1
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = AppProperties.PREFIX)
public class AppProperties {

    public static final String PREFIX = "app";

    private boolean enable = true;

//    private AppCommonProperties common = new AppCommonProperties();

    // private AppJpaProperties jpa = new AppJpaProperties();
}
