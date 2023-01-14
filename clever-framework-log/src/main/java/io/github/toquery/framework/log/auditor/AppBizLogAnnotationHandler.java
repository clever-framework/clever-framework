package io.github.toquery.framework.log.auditor;

import com.google.common.collect.Maps;
import io.github.toquery.framework.common.util.AppJacksonUtils;
import io.github.toquery.framework.core.log.annotation.AppLogField;
import io.github.toquery.framework.core.log.annotation.AppLogFieldIgnore;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.entity.AppBaseEntity;
import io.github.toquery.framework.log.properties.AppLogProperties;
import io.github.toquery.framework.log.entity.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import jakarta.annotation.Resource;
import jakarta.persistence.Transient;
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
        sysLog.setRawData(AppJacksonUtils.object2String(rawData));
        sysLog.setTargetData(AppJacksonUtils.object2String(targetData));
        sysLog.setLogType(logType);
        sysLog.setOperateDateTime(LocalDateTime.now());
        return sysLog;
    }


    public Set<Field> handleEntityFields(AppBaseEntity appBaseEntity) {
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
