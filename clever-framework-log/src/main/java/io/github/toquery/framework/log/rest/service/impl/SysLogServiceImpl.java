package io.github.toquery.framework.log.rest.service.impl;

import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.log.rest.dao.SysLogRepository;
import io.github.toquery.framework.log.rest.entity.SysLog;
import io.github.toquery.framework.log.rest.service.ISysLogService;
import org.springframework.stereotype.Service;

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
            put("createDateGT", "createDate:GTDATE");
            put("createDateLT", "createDate:LTDATE");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressions;
    }

}
