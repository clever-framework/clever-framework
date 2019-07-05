package io.github.toquery.framework.log.rest.dao;

import io.github.toquery.framework.dao.jpa.annotation.MybatisQuery;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.log.rest.entity.SysLog;
import org.apache.ibatis.annotations.Param;

/**
 * @author toquery
 * @version 1
 */
//@Mapper
//@Component
public interface SysLogRepository extends AppJpaBaseRepository<SysLog, Long> {

    @MybatisQuery
    SysLog getById(@Param("id") Long id);

//    @MybatisQuery
//    SysLog save(@Param("sysLog") SysLog sysLog);

//    @MybatisQuery
//    Page<SysLog> query();

}
