package io.github.toquery.framework.log.dao;

import com.github.pagehelper.Page;
import io.github.toquery.framework.dao.jpa.annotation.MybatisQuery;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.log.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author toquery
 * @version 1
 */
//@Mapper
//@Component
public interface SysLogRepository extends AppJpaBaseRepository<SysLog, Long> {

//    @MybatisQuery
//    SysLog getById(@Param("id") Long id);

//    @MybatisQuery
//    SysLog save(@Param("sysLog") SysLog sysLog);

//    @MybatisQuery
//    Page<SysLog> query();

}
