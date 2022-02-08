package io.github.toquery.framework.datasource.aop;

import io.github.toquery.framework.datasource.DataSourceSwitch;
import io.github.toquery.framework.datasource.config.DataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Slf4j
@Aspect
public class DynamicDataSourceAspect {

    public DynamicDataSourceAspect() {
        log.debug("DynamicDataSourceAspect init ...");
    }

    @Pointcut(value = "@annotation(dataSource))", argNames = "dataSource")
    private void dataSourceAnnotation(DataSourceSwitch dataSource) {
    }

    @Before(value = "dataSourceAnnotation(dataSource)", argNames = "dataSource")
    public void around(DataSourceSwitch dataSource) throws Throwable {
        DataSourceContextHolder.setDataSource(dataSource.value());
        log.info("switch to {} datasource ...", dataSource.value());
    }

    @Around(value = "dataSourceAnnotation(dataSource)", argNames = "point,dataSource")
    public Object around(ProceedingJoinPoint point, DataSourceSwitch dataSource) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        DataSourceSwitch classDataSourceSwitch = signature.getClass().getAnnotation(DataSourceSwitch.class);
        DataSourceSwitch methodDataSourceSwitch = method.getAnnotation(DataSourceSwitch.class);
        // 优先使用方法注解，再使用类注解
        if (methodDataSourceSwitch != null) {
            DataSourceContextHolder.setDataSource(methodDataSourceSwitch.value());
        } else if (classDataSourceSwitch != null) {
            DataSourceContextHolder.setDataSource(classDataSourceSwitch.value());
        }

        log.debug("set datasource is " + DataSourceContextHolder.getDataSource());

        try {
            return point.proceed();
        } finally {
            DataSourceContextHolder.clear();
            log.debug("clean datasource");
        }
    }

}
