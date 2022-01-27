package io.github.toquery.framework.datasource.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Data
@ConfigurationProperties(prefix = AppDataSourcesProperties.PREFIX)
public class AppDataSourcesProperties {

    public static final String PREFIX = "app.datasource";

    private Map<String, DataSourceProperties> multiple;

}
