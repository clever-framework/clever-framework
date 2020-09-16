package io.github.toquery.framework.data.rest.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = AppDataRestProperties.PREFIX)
public class AppDataRestProperties {

    public static final String PREFIX = "app.data.rest";
}
