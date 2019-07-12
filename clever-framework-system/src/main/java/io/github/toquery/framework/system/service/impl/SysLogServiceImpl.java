package io.github.toquery.framework.system.service.impl;

import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.domain.SysLog;
import io.github.toquery.framework.system.repository.SysLogRepository;
import io.github.toquery.framework.system.service.ISysLogService;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
public class SysLogServiceImpl extends AppBaseServiceImpl<Long, SysLog, SysLogRepository> implements ISysLogService {


    private static final Map<String, String> expressions = new LinkedHashMap<String, String>() {
        {
            put("moduleName", "moduleName:EQ");
            put("bizName", "bizName:EQ");
            put("logType", "logType:EQ");
            put("moduleNameLIKE", "moduleName:LIKE");
            put("bizNameLIKE", "bizName:LIKE");
            put("createDateGT", "createDate:GTDATE");
            put("createDateLT", "createDate:LTDATE");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressions;
    }

}
