package io.github.toquery.framework.datasource.aop;

import io.github.toquery.framework.datasource.DataSourceSwitch;
import io.github.toquery.framework.datasource.config.DataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
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

    @Pointcut("@annotation(io.github.toquery.framework.datasource.DataSourceSwitch)")
    public void dataSourceSwitchPointCut() {
    }
//    @Pointcut(value = "@within(dataSource) || @annotation(dataSource))", argNames = "dataSource")
//    public void dataSourceAnnotation(DataSourceSwitch dataSource) {
//    }

    @Before(value = "dataSourceSwitchPointCut() && @annotation(dataSource)", argNames = "joinPoint,dataSource")
    public void before(JoinPoint joinPoint, DataSourceSwitch dataSource) throws Throwable {
        log.info("switch to datasource ...");
    }

    // @Around(value = "dataSourceAnnotation(dataSource)", argNames = "point,dataSource")
    public Object around(ProceedingJoinPoint point, DataSourceSwitch dataSource) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        DataSourceSwitch classDataSourceSwitch = point.getTarget().getClass().getAnnotation(DataSourceSwitch.class);
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
