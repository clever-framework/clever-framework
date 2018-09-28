package io.github.toquery.framework.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author toquery
 * @version 1
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.jpa")
public class JpaProperties {

    private boolean enable = true;
}
