
package io.github.toquery.framework.datasource.jpa;

import io.github.toquery.framework.datasource.config.DataSourceContextHolder;
import io.github.toquery.framework.datasource.config.DynamicDataSourceRouter;
import io.github.toquery.framework.datasource.properties.AppDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DynamicDataSourceRouterConfiguration {

    @Resource
    private DataSource dataSource;

    @Resource
    private AppDataSourceProperties appDataSourceProperties;

    public DynamicDataSourceRouterConfiguration() {
        log.debug("DynamicDataSourceRouterConfiguration init");
    }

    @Bean(name = "routingDataSource")
    @ConditionalOnMissingBean
    public DynamicDataSourceRouter routingDataSource() {
        DynamicDataSourceRouter proxy = new DynamicDataSourceRouter();
        Map<String, DataSourceProperties> multiple = appDataSourceProperties.getMultiple();
        Map<Object, Object> targetDataSources = new HashMap<>(multiple.size());
        targetDataSources.put(DataSourceContextHolder.DEFAULT_DATA_SOURCE, dataSource);
        multiple.forEach((k, dataSourceProperties) -> {
            DataSourceBuilder<?> factory = DataSourceBuilder.create()
                    .type(dataSourceProperties.getType())
                    .driverClassName(dataSourceProperties.getDriverClassName())
                    .url(dataSourceProperties.getUrl())
                    .username(dataSourceProperties.getUsername())
                    .password(dataSourceProperties.getPassword());
            targetDataSources.put(k, factory.build());
        });
        proxy.setTargetDataSources(targetDataSources);
        return proxy;
    }

}
