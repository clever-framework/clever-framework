package io.github.toquery.framework.system.repository;

import io.github.toquery.framework.dao.jpa.annotation.MybatisQuery;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.system.entity.SysLog;
import org.apache.ibatis.annotations.Param;

/**
 * @author toquery
 * @version 1
 */

public interface SysLogRepository extends AppJpaBaseRepository<SysLog, Long> {

    @MybatisQuery
    public int insertSysLog(@Param("sysLog") SysLog sysLog);

}
