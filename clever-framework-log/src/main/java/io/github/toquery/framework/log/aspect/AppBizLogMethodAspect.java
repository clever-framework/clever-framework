package io.github.toquery.framework.log.aspect;

import com.google.common.base.Strings;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.log.auditor.AppBizLogAnnotationHandler;
import io.github.toquery.framework.log.entity.SysLog;
import io.github.toquery.framework.log.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 实现AOP的切面主要有以下几个要素：
 * around , before , around , after , afterReturning
 *
 * <p>
 * 使用@Aspect注解将一个java类定义为切面类
 * 使用@Pointcut定义一个切入点，可以是一个规则表达式，比如下例中某个package下的所有函数，也可以是一个注解等。
 * 根据需要在切入点不同位置的切入内容
 * 使用@Before在切入点开始处切入内容
 * 使用@After在切入点结尾处切入内容
 * 使用@AfterReturning在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
 * 使用@Around在切入点前后切入内容，并自己控制何时执行切入点自身的内容
 * 使用@AfterThrowing用来处理当切入内容部分抛出异常之后的处理逻辑
 * </p>
 *
 * <p>
 * 日志拦截规则：
 * 1. 根据注解内配置（$），未配置 $ 则直接获取注解字段
 * 2. 配置 $ 后，首先获取方法参数，为空时获取实体字段值
 * </p>
 *
 * @author toquery
 * @version 1
 */
@Slf4j
@Aspect
@Component
public class AppBizLogMethodAspect {

    private static final String INVOKE_FIELD_PREFIX = "$";

    @Resource
    private HttpServletRequest request;

    @Resource
    private ISysLogService sysLogService;

    @Resource
    private AppBizLogAnnotationHandler appBizLogAnnotationHandler;


    @Pointcut(value = "@annotation(appLogMethod)", argNames = "appLogMethod")
    public void pointcut(AppLogMethod appLogMethod) {
    }


    @AfterReturning(returning = "response", pointcut = "pointcut(appLogMethod)", argNames = "joinPoint,appLogMethod,response")
    public void doAfterReturning(JoinPoint joinPoint, AppLogMethod appLogMethod, Object response) {
        try {
            this.handleBizLog(joinPoint, appLogMethod);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存业务日志失败，操作类: {} 方法：{} \n 参数： {}", joinPoint.getTarget().getClass().toString(), joinPoint.getSignature().getName(), JacksonUtils.object2String(joinPoint.getArgs()));
        }
    }


    private void handleBizLog(JoinPoint joinPoint, AppLogMethod appLogMethod) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        AppBaseEntity appBaseEntity = this.getAppBaseEntity(joinPoint.getArgs());

        String modelName = appLogMethod.modelName().startsWith(INVOKE_FIELD_PREFIX) ? this.invokeFieldValue(appLogMethod.modelName(), appBaseEntity) : appLogMethod.modelName();
        String bizName = appLogMethod.bizName().startsWith(INVOKE_FIELD_PREFIX) ? this.invokeFieldValue(appLogMethod.bizName(), appBaseEntity) : appLogMethod.bizName();

        Map<String, Object> targetData = appBizLogAnnotationHandler.handleTargetData(appBaseEntity, appBizLogAnnotationHandler.handleEntityFields(appBaseEntity, null));

        SysLog sysLog = appBizLogAnnotationHandler.fill2SysLog(appLogMethod.logType(), null, targetData,
                Strings.isNullOrEmpty(modelName) ? appLogMethod.modelName() : modelName,
                Strings.isNullOrEmpty(bizName) ? appLogMethod.bizName() : bizName);
        sysLog = sysLogService.save(sysLog);
        log.debug("保存业务日志成功 AppBizLogMethodAspect -> doAfterReturning -> handleBizLog 操作类: {} 方法：{}", joinPoint.getTarget().getClass().toString(), joinPoint.getSignature().getName());
    }

    //获取请求中携带的参数，如果为空则反射获取实体字段的值
    private String invokeFieldValue(String fieldName, AppBaseEntity appBaseEntity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        fieldName = fieldName.substring(1);
        // 方法参数值
        String paramValue = request.getParameter(fieldName);
        return Strings.isNullOrEmpty(paramValue) ? (String) PropertyUtils.getProperty(appBaseEntity, fieldName) : paramValue;
    }

    private AppBaseEntity getAppBaseEntity(Object[] argObjects) {
        if (argObjects == null || argObjects.length <= 0) {
            return null;
        }
        Optional<AppBaseEntity> appBaseEntityOptional = Stream.of(argObjects).filter(item -> item instanceof AppBaseEntity).map(item -> (AppBaseEntity) item).findAny();
        // 如果接受参数为单个实体，则去获取单个实体
        return appBaseEntityOptional.orElseGet(null);
    }
}
