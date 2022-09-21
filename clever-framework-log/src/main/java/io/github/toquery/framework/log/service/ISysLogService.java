package io.github.toquery.framework.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.log.entity.SysLog;

import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
public interface ISysLogService extends IService<SysLog> {

    public int insertSysLog(Long userId, String userName, String moduleName, String bizName, AppLogType logType, String rawData, String targetData);

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
