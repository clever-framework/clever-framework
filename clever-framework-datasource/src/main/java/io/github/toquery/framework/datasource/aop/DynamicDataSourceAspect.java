package io.github.toquery.framework.datasource.aop;

import io.github.toquery.framework.datasource.DataSource;
import io.github.toquery.framework.datasource.config.DataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class DynamicDataSourceAspect {

    public DynamicDataSourceAspect() {
        log.debug("DynamicDataSourceAspect init ...");
    }

    @Pointcut(value = "@annotation(dataSource))", argNames = "dataSource")
    private void dataSourceAnnotation(DataSource dataSource) {
    }

    @Before(value = "dataSourceAnnotation(dataSource)", argNames = "dataSource")
    public void around(DataSource dataSource) throws Throwable {
        DataSourceContextHolder.setDataSource(dataSource.value());
        log.info("switch to {} datasource ...", dataSource.value());
        try {
        } finally {
            log.info("clean datasource router ...");
            //DataSourceContextHolder.clear();
        }
    }

}
