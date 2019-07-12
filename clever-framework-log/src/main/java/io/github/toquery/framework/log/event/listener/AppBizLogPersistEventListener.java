package io.github.toquery.framework.log.event.listener;

import com.alibaba.fastjson.JSON;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.core.annotation.AppLogEntity;
import io.github.toquery.framework.log.auditor.AppBizLogAnnotationHandler;
import io.github.toquery.framework.core.constant.AppLogType;
import io.github.toquery.framework.system.domain.SysLog;
import io.github.toquery.framework.system.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Scope("singleton")
public class AppBizLogPersistEventListener implements PersistEventListener {


    @Resource
    private ISysLogService sysLogService;

    @Resource
    private AppBizLogAnnotationHandler appBizLogAnnotationHandler;

    @Override
    public void onPersist(PersistEvent event) throws HibernateException {
        Object entity = event.getObject();
        if (entity instanceof SysLog) {
            log.debug("接收到新增 SysLog 的数据操作，但不会记录日志中。");
        } else if (entity instanceof AppBaseEntity) {
            log.debug("接收到新增 {} 的数据操作，将记录日志。", entity.getClass().getSimpleName());
            AppBaseEntity appBaseEntity = (AppBaseEntity) entity;
            AppLogEntity appLogEntity = appBizLogAnnotationHandler.handleEntityAnnotation(appBaseEntity);
            if (appLogEntity == null) {
                return;
            }
            Map<String, Object> targetData = appBizLogAnnotationHandler.handleTargetData(appBaseEntity, appBizLogAnnotationHandler.handleEntityFields(appBaseEntity, appLogEntity));
            SysLog sysLog = appBizLogAnnotationHandler.fill2SysLog(appBaseEntity, null, targetData, appLogEntity.modelName(), appLogEntity.bizName(), AppLogType.CREA);
            sysLogService.save(sysLog);
            log.debug("接收到新增 {} 的数据操作，记录日志完成。", entity.getClass().getSimpleName());
        } else {
            log.warn("处理对象 {} 解析失败，将不记录审查日志。", entity.getClass().getSimpleName());
        }
    }

    @Override
    public void onPersist(PersistEvent event, Map createdAlready) throws HibernateException {
        log.debug("接收到 createdAlready 数据。\n {}", JSON.toJSONString(createdAlready));
        this.onPersist(event);
    }
}