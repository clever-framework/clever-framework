package io.github.toquery.framework.datasource.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class DynamicDataSourceRouter extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String datSourceKey = DataSourceContextHolder.getDataSource();
        log.debug("当前数据源：{}", datSourceKey);
        return datSourceKey;
    }
}
