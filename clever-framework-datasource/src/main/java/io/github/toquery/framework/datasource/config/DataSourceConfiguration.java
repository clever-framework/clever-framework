/*
package io.github.toquery.framework.datasource.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DataSourceConfiguration {

    private final static String SLAVE_DATASOURCE_KEY = "slaveDataSource";

    @Value("${app.datasource.type}")
    private Class<? extends DataSource> dataSourceType;

    public DataSourceConfiguration() {
        log.info("create datasource configuration...");
    }

    @Bean(value = SLAVE_DATASOURCE_KEY)
    @Qualifier(SLAVE_DATASOURCE_KEY)
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        log.info("create slave datasource...");
        return DataSourceBuilder.create()
                .type(dataSourceType)
                .build();
    }

}
*/
