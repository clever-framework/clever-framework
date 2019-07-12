package io.github.toquery.framework.log.aspect;

import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.core.annotation.AppLogMethod;
import io.github.toquery.framework.log.auditor.AppBizLogAnnotationHandler;
import io.github.toquery.framework.system.domain.SysLog;
import io.github.toquery.framework.system.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 实现AOP的切面主要有以下几个要素：
 * around , before , around , after , afterReturning
 * <p>
 * 使用@Aspect注解将一个java类定义为切面类
 * 使用@Pointcut定义一个切入点，可以是一个规则表达式，比如下例中某个package下的所有函数，也可以是一个注解等。
 * 根据需要在切入点不同位置的切入内容
 * 使用@Before在切入点开始处切入内容
 * 使用@After在切入点结尾处切入内容
 * 使用@AfterReturning在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
 * 使用@Around在切入点前后切入内容，并自己控制何时执行切入点自身的内容
 * 使用@AfterThrowing用来处理当切入内容部分抛出异常之后的处理逻辑
 *
 * @author toquery
 * @version 1
 */
@Slf4j
@Aspect
@Component
public class AppBizLogMethodAspect {

    @Resource
    private ISysLogService sysLogService;

    @Resource
    private AppBizLogAnnotationHandler appBizLogAnnotationHandler;


    @Pointcut(value = "@annotation(appLogMethod)", argNames = "appLogMethod")
    public void pointcut(AppLogMethod appLogMethod) {
    }


    @AfterReturning(returning = "response", pointcut = "pointcut(appLogMethod)", argNames = "joinPoint,appLogMethod,response")
    public void doAfterReturning(JoinPoint joinPoint, AppLogMethod appLogMethod, Object response) throws Throwable {
//        Signature signature = joinPoint.getSignature();
        AppBaseEntity argBaseEntity = this.argObjectsInclude(joinPoint.getArgs());
        if (argBaseEntity == null) {
            log.info("记录日志失败，获取到参数类型不正确。");
            return;
        }
        String modelName = appLogMethod.modelName();

        if (invokeEntityField(modelName)) {
            modelName = invokeEntityFieldValue(modelName, argBaseEntity);
        }

        String bizName = appLogMethod.bizName();
        if (invokeEntityField(bizName)) {
            bizName = invokeEntityFieldValue(bizName, argBaseEntity);
        }
        Map<String, Object> targetData = appBizLogAnnotationHandler.handleTargetData(argBaseEntity, appBizLogAnnotationHandler.handleEntityFields(argBaseEntity, null));

        SysLog sysLog = appBizLogAnnotationHandler.fill2SysLog(argBaseEntity, null, targetData, modelName, bizName, appLogMethod.logType());

        sysLogService.save(sysLog);
        log.info("doAfterReturning");
    }

    private boolean invokeEntityField(String fieldName) {
        return fieldName.startsWith("$");
    }

    private String invokeEntityFieldValue(String fieldName, AppBaseEntity appBaseEntity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        fieldName = fieldName.substring(1);
        return (String) PropertyUtils.getProperty(appBaseEntity, fieldName);
    }

    private AppBaseEntity argObjectsInclude(Object[] argObjects) {
        if (argObjects == null || argObjects.length <= 0) {
            return null;
        }
        Optional<AppBaseEntity> appBaseEntityOptional = Stream.of(argObjects).filter(item -> item instanceof AppBaseEntity).map(item -> (AppBaseEntity) item).findAny();
        Optional<Collection<AppBaseEntity>> appBaseEntityCollection = Stream.of(argObjects).filter(item -> item instanceof Collection).map(item -> (Collection<AppBaseEntity>) item).findAny();

        // 如果接受参数为单个实体，则去获取单个实体
        // 如果接收参数为List，则去获取list的任意一个
        return appBaseEntityOptional.orElseGet(() -> appBaseEntityCollection.map(appBaseEntities -> appBaseEntities.stream().filter(Objects::nonNull).findAny().orElse(null)).orElse(null));
    }


    //后置异常通知
    @AfterThrowing(value = "pointcut(appLogMethod)", argNames = "joinPoint,ex,appLogMethod", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex, AppLogMethod appLogMethod) {
        log.info("afterThrowing");
    }


}
