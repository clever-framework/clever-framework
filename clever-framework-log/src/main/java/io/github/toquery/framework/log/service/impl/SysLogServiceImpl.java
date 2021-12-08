package io.github.toquery.framework.log.service.impl;

import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.dao.primary.snowflake.SnowFlake;
import io.github.toquery.framework.log.entity.SysLog;
import io.github.toquery.framework.log.repository.SysLogRepository;
import io.github.toquery.framework.log.service.ISysLogService;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
public class SysLogServiceImpl extends AppBaseServiceImpl<SysLog, SysLogRepository> implements ISysLogService {


    private static final Map<String, String> expressions = new LinkedHashMap<String, String>() {
        {
            put("moduleName", "moduleName:EQ");
            put("bizName", "bizName:EQ");
            put("logType", "logType:EQ");
            put("moduleNameLIKE", "moduleName:LIKE");
            put("bizNameLIKE", "bizName:LIKE");
            put("createDateTimeGT", "createDateTime:GTDATE");
            put("createDateTimeLT", "createDateTime:LTDATE");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressions;
    }


    public int insertSysLog(Long userId, String moduleName, String bizName, AppLogType logType, String rawData, String targetData) {
        SysLog sysLog = new SysLog();

        sysLog.setModuleName(moduleName);
        sysLog.setBizName(bizName);
        sysLog.setRawData(rawData);
        sysLog.setTargetData(targetData);
        sysLog.setUserId(userId);
        sysLog.setOptionDateTime(LocalDateTime.now());

        if (logType == null) {
            sysLog.setLogType(AppLogType.CREATE);
        }

        LocalDateTime now = LocalDateTime.now();
        sysLog.setCreateDateTime(now);
        sysLog.setUpdateDateTime(now);
        if (userId == null) {
            sysLog.setCreateUserId(0L);
            sysLog.setUpdateUserId(0L);
        } else {
            sysLog.setCreateUserId(userId);
            sysLog.setUpdateUserId(userId);
        }

        sysLog.preInsert();
        sysLog.setId(new SnowFlake().nextId());
        return repository.insertSysLog(sysLog);
    }
}
