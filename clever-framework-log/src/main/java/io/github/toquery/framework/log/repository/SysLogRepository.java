package io.github.toquery.framework.log.repository;

import io.github.toquery.framework.dao.annotation.MybatisQuery;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.log.entity.SysLog;
import org.apache.ibatis.annotations.Param;

/**
 * @author toquery
 * @version 1
 */

public interface SysLogRepository extends AppJpaBaseRepository<SysLog> {

    @MybatisQuery
    public int insertSysLog(@Param("sysLog") SysLog sysLog);

}
