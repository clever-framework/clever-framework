package io.github.toquery.framework.log.service;

import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.log.entity.SysLog;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
public interface ISysLogService extends AppBaseService<SysLog> {

    public int insertSysLog(Long userId, String moduleName, String bizName, AppLogType logType, String rawData, String targetData);

    /**
     * 查询日志
     *
     * @param params
     * @param pageCurrent
     * @param pageSize
     * @return
     */
    Page<SysLog> pageWithUser(Map<String, Object> params, int pageCurrent, int pageSize, String[] pageSort);
}
