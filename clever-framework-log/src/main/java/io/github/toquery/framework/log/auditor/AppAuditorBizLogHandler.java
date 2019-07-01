package io.github.toquery.framework.log.auditor;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import io.github.toquery.framework.dao.audit.AppAuditorHandler;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.log.annotation.AppLogEntity;
import io.github.toquery.framework.log.annotation.AppLogField;
import io.github.toquery.framework.log.annotation.AppLogIgnoreField;
import io.github.toquery.framework.log.constant.AppLogType;
import io.github.toquery.framework.log.entity.SysLog;
import io.github.toquery.framework.log.properties.AppLogProperties;
import io.github.toquery.framework.log.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ApplicationContextAware 获取 ApplicationContext
 * ApplicationListener ContextRefreshedEvent : Spring 容器加载完毕后，执行操作
 *
 * @author toquery
 * @version 1
 */
@Slf4j
@Component
@Scope("singleton")
public class AppAuditorBizLogHandler implements AppAuditorHandler, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent>, InitializingBean {

    @Resource
    private AppLogProperties appLogProperties;

    // Spring 容器加载完毕后 set bean
    private ISysLogService sysLogService;


    public AppAuditorBizLogHandler() {
        log.info("初始化 App Biz Log 审计日志");
    }

    @Override
    public boolean enable() {
        log.debug("当前配置中，系统日志状态为：{}", appLogProperties.isEnable());
        return appLogProperties.isEnable();
    }


    private SysLog fill2SysLog(AppBaseEntity appBaseEntity, Map<String, Object> rawData, Map<String, Object> targetData, AppLogEntity appLogEntity, AppLogType logType) {
        SysLog sysLog = new SysLog();
        sysLog.setModuleName(appLogEntity.modelName());
        sysLog.setBizName(appLogEntity.bizName());
        sysLog.setRawData(JSON.toJSONString(rawData));
        sysLog.setTargetData(JSON.toJSONString(targetData));
        sysLog.setLogType(logType);
        sysLog.setUserId(appBaseEntity.getLastUpdateUserId());
        return sysLog;
    }

    private AppLogEntity handleEntityAnnotation(AppBaseEntity appBaseEntity) {
        // 获取当前类，或父类的注解
        AppLogEntity appLogEntity = appBaseEntity.getClass().getAnnotation(AppLogEntity.class);
        // AppLogEntity superAppLogEntity = appBaseEntity.getClass().getSuperclass().getAnnotation(AppLogEntity.class);
        if (appLogEntity == null) {
            log.debug("当前实体类 {} , 未配置 @AppLogEntity 注解，将不记录日志", appBaseEntity.getClass().getSimpleName());
            return null;
        }
        log.debug("当前实体类 {}, 已配置 @AppLogEntity 注解，将记录日志, modelName = {}, bizName = {}", appBaseEntity.getClass().getSimpleName(), appLogEntity.modelName(), appLogEntity.bizName());

        return appLogEntity;
    }


    private Set<Field> handleEntityFields(AppBaseEntity appBaseEntity, AppLogEntity appLogEntity) {

        // 标识为 AppLogIgnoreField、Transient 或者 在全局设置中的字段 将不记录日志
        return Arrays.stream(appBaseEntity.getClass().getDeclaredFields())
                .filter(item -> item.getAnnotation(AppLogIgnoreField.class) == null && item.getAnnotation(Transient.class) == null && !appLogProperties.getIgnoreFields().contains(item.getName()))
                .map(item -> {
                    // 设置 private 为可访问
                    item.setAccessible(true);
                    return item;
                }).collect(Collectors.toSet());
    }


    private Map<String, Object> handleTargetData(AppBaseEntity appBaseEntity, Set<Field> fieldSet) {
        Map<String, Object> targetData = Maps.newHashMap();

        // 是否存在唯一标识
        boolean hasUniqueFlag = fieldSet.stream().anyMatch(field -> {
            AppLogField appLogField = field.getAnnotation(AppLogField.class);
            return appLogField != null && appLogField.uniqueFlag();
        });

        if (hasUniqueFlag) {
            fieldSet.stream().filter(field -> field.getAnnotation(AppLogField.class) != null && field.getAnnotation(AppLogField.class).uniqueFlag()).forEach(field -> {
                AppLogField appLogField = field.getAnnotation(AppLogField.class);
                Object value = ReflectionUtils.getField(field, appBaseEntity);
                targetData.put(appLogField.value(), value);
            });
        } else {
            fieldSet.forEach(field -> {
                AppLogField appLogField = field.getAnnotation(AppLogField.class);
                String key = appLogField == null ? field.getName() : appLogField.value();
                Object value = ReflectionUtils.getField(field, appBaseEntity);
                targetData.put(key, value);
            });
        }

        return targetData;
    }

    @Override
    public void onPrePersist(AppBaseEntity appBaseEntity) {
        log.debug("接收到新增数据操作，将记录日志。");
        AppLogEntity appLogEntity = handleEntityAnnotation(appBaseEntity);
        if (appLogEntity == null) {
            return;
        }

        Map<String, Object> targetData = handleTargetData(appBaseEntity, handleEntityFields(appBaseEntity, appLogEntity));

        SysLog sysLog = this.fill2SysLog(appBaseEntity, null, targetData, appLogEntity, AppLogType.CREA);

        sysLogService.save(sysLog);
        log.debug("接收到");
    }

    @Override
    public void onPreUpdate(AppBaseEntity appBaseEntity) {
        log.debug("接收到修改数据操作，将记录日志。");
        AppLogEntity appLogEntity = handleEntityAnnotation(appBaseEntity);
        if (appLogEntity == null) {
            return;
        }

        Map<String, Object> targetData = handleTargetData(appBaseEntity, handleEntityFields(appBaseEntity, appLogEntity));
        SysLog sysLog = this.fill2SysLog(appBaseEntity, null, targetData, appLogEntity, AppLogType.MODF);
        sysLogService.save(sysLog);
        log.debug("接收到");

    }

    @Override
    public void onPreRemove(AppBaseEntity appBaseEntity) {
        log.debug("接收到删除数据操作，将记录日志。");
        AppLogEntity appLogEntity = handleEntityAnnotation(appBaseEntity);
        if (appLogEntity == null) {
            return;
        }
        Map<String, Object> targetData = handleTargetData(appBaseEntity, handleEntityFields(appBaseEntity, appLogEntity));
        SysLog sysLog = this.fill2SysLog(appBaseEntity, null, targetData, appLogEntity, AppLogType.DEL);
        sysLogService.save(sysLog);
        log.debug("接收到");
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        log.debug("初始化 App Biz Log 审计日志，设置 ApplicationContext");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("初始化 App Biz Log 审计日志，设置 afterProperties");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.sysLogService = applicationContext.getBean(ISysLogService.class);
        log.debug("系统启动成功，获取 ApplicationContext 成功，获取审核日志服务成功！");
    }
}
