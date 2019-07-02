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
public class AppAuditorBizLogHandler extends AppAuditorBizLogAnnotationHandler implements AppAuditorHandler, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent>, InitializingBean {

    @Resource
    private AppLogProperties appLogProperties;

    private ApplicationContext applicationContext;

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
    public void onPostLoad(AppBaseEntity appBaseEntity) {

    }

    @Override
    public void onPostPersist(AppBaseEntity appBaseEntity) {

    }

    @Override
    public void onPostUpdate(AppBaseEntity appBaseEntity) {

    }

    @Override
    public void onPostRemove(AppBaseEntity appBaseEntity) {

    }

    @Override
    public int getOrder() {
        return 0;
    }


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
