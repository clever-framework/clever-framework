package io.github.toquery.framework.log.event.listener;

import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.core.log.annotation.AppLogEntity;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.log.auditor.AppBizLogAnnotationHandler;
import io.github.toquery.framework.system.entity.SysLog;
import io.github.toquery.framework.system.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.DeleteEvent;
import org.hibernate.event.spi.DeleteEventListener;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Scope("singleton")
public class AppBizLogDeleteEventListener implements DeleteEventListener {

    @Resource
    private ISysLogService sysLogService;

    @Resource
    private AppBizLogAnnotationHandler appBizLogAnnotationHandler;

    @Override
    public void onDelete(DeleteEvent event) throws HibernateException {
        AppBaseEntity appBaseEntity = (AppBaseEntity) event.getObject();

        log.debug("接收到删除数据操作，将记录日志。");
        AppLogEntity appLogEntity = appBizLogAnnotationHandler.handleEntityAnnotation(appBaseEntity);
        if (appLogEntity == null) {
            return;
        }
        Map<String, Object> targetData = appBizLogAnnotationHandler.handleTargetData(appBaseEntity, appBizLogAnnotationHandler.handleEntityFields(appBaseEntity, appLogEntity));
        SysLog sysLog = appBizLogAnnotationHandler.fill2SysLog(AppLogType.DEL, null, targetData, appLogEntity.modelName(), appLogEntity.bizName());
        sysLogService.save(sysLog);
        log.debug("接收到");
    }

    @Override
    public void onDelete(DeleteEvent event, Set transientEntities) throws HibernateException {
        log.debug("接收到 transientEntities 数据。\n {}", JacksonUtils.object2String(transientEntities));
        this.onDelete(event);
    }
}
