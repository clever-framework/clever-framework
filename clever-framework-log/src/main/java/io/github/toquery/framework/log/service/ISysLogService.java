package io.github.toquery.framework.log.service;

import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.log.entity.SysLog;

/**
 * @author toquery
 * @version 1
 */
public interface ISysLogService extends AppBaseService<SysLog, Long> {

    public int insertSysLog(Long userId, String moduleName, String bizName, AppLogType logType, String rawData, String targetData);
}
