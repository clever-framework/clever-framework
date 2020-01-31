package io.github.toquery.framework.system.service.impl;

import io.github.toquery.framework.core.constant.AppLogType;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.dao.primary.snowflake.SnowFlake;
import io.github.toquery.framework.system.entity.SysLog;
import io.github.toquery.framework.system.repository.SysLogRepository;
import io.github.toquery.framework.system.service.ISysLogService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Service
public class SysLogServiceImpl extends AppBaseServiceImpl<Long, SysLog, SysLogRepository> implements ISysLogService {


    private static final Map<String, String> expressions = new LinkedHashMap<String, String>() {
        {
            put("moduleName", "moduleName:EQ");
            put("bizName", "bizName:EQ");
            put("logType", "logType:EQ");
            put("moduleNameLIKE", "moduleName:LIKE");
            put("bizNameLIKE", "bizName:LIKE");
            put("createDatetimeGT", "createDatetime:GTDATE");
            put("createDatetimeLT", "createDatetime:LTDATE");
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

        if (logType == null) {
            sysLog.setLogType(AppLogType.CREA);
        }

        Date date = new Date();
        sysLog.setCreateDatetime(date);
        sysLog.setLastUpdateDatetime(date);
        if (userId == null) {
            sysLog.setCreateUserId(0L);
            sysLog.setLastUpdateUserId(0L);
        } else {
            sysLog.setCreateUserId(userId);
            sysLog.setLastUpdateUserId(userId);
        }

        sysLog.setId(new SnowFlake().nextId());
        return entityDao.insertSysLog(sysLog);
    }
}
