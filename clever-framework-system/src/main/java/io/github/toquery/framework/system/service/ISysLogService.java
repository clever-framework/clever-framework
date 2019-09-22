package io.github.toquery.framework.system.service;

import io.github.toquery.framework.core.constant.AppLogType;
import io.github.toquery.framework.curd.service.AppBaseService;
import io.github.toquery.framework.system.entity.SysLog;

/**
 * @author toquery
 * @version 1
 */
public interface ISysLogService extends AppBaseService<SysLog,Long> {

    public int insertSysLog(Long userId,String moduleName, String bizName, AppLogType logType, String rawData, String targetData);
}
