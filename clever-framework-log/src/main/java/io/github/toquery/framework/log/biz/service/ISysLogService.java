package io.github.toquery.framework.log.biz.service;

import com.github.pagehelper.Page;
import io.github.toquery.framework.log.biz.entity.SysLog;
// import io.github.toquery.cleverweb.MenuDao;

/**
 * @author toquery
 * @version 1
 */
public interface ISysLogService {

    Page<SysLog> handleQuery();

    SysLog save(SysLog sysLog);

    SysLog getById(Long id);
}
