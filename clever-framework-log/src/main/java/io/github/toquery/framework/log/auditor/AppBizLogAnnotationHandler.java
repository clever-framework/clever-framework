package io.github.toquery.framework.log.auditor;

import com.google.common.collect.Maps;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.core.log.annotation.AppLogEntity;
import io.github.toquery.framework.core.log.annotation.AppLogEntityIgnore;
import io.github.toquery.framework.core.log.annotation.AppLogField;
import io.github.toquery.framework.core.log.annotation.AppLogFieldIgnore;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.entity.AppBaseEntity;
import io.github.toquery.framework.log.properties.AppLogProperties;
import io.github.toquery.framework.log.entity.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class AppBizLogAnnotationHandler {

    @Resource
    private AppLogProperties appLogProperties;

    public SysLog fill2SysLog(AppLogType logType, Map<String, Object> rawData, Map<String, Object> targetData, String modelName, String bizName) {
        SysLog sysLog = new SysLog();
        sysLog.setModuleName(modelName);
        sysLog.setBizName(bizName);
        sysLog.setRawData(JacksonUtils.object2String(rawData));
        sysLog.setTargetData(JacksonUtils.object2String(targetData));
        sysLog.setLogType(logType);
        sysLog.setOperateDateTime(LocalDateTime.now());
        return sysLog;
    }

    public AppLogEntity handleEntityAnnotation(AppBaseEntity appBaseEntity) {
        // 获取当前类，或父类的注解
        AppLogEntity appLogEntity = appBaseEntity.getClass().getAnnotation(AppLogEntity.class);
        // 忽略实体业务日志
        AppLogEntityIgnore appLogEntityIgnore = appBaseEntity.getClass().getAnnotation(AppLogEntityIgnore.class);

        if (appLogEntity == null || appLogEntityIgnore != null) {
            log.debug("当前实体类 {} , 未配置 @AppLogEntity 注解，将不记录日志", appBaseEntity.getClass().getSimpleName());
            return null;
        }
        log.debug("当前实体类 {}, 已配置 @AppLogEntity 注解，将记录日志, modelName = {}, bizName = {}", appBaseEntity.getClass().getSimpleName(), appLogEntity.modelName(), appLogEntity.bizName());
        return appLogEntity;
    }


    public Set<Field> handleEntityFields(AppBaseEntity appBaseEntity, AppLogEntity appLogEntity) {
        // 标识为 AppLogIgnoreField、Transient 或者 在全局设置中的字段 将不记录日志
        return Arrays.stream(appBaseEntity.getClass().getDeclaredFields())
                .filter(item -> item.getAnnotation(AppLogFieldIgnore.class) == null && item.getAnnotation(Transient.class) == null && !appLogProperties.getIgnoreFields().contains(item.getName()))
                .map(item -> {
                    // 设置 private 为可访问
                    item.setAccessible(true);
                    return item;
                }).collect(Collectors.toSet());
    }


    public Map<String, Object> handleTargetData(AppBaseEntity appBaseEntity, Set<Field> fieldSet) {
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

}
