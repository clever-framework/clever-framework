package io.github.toquery.framework.log.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.log.entity.SysLog;
import org.apache.ibatis.annotations.Param;

/**
 * @author toquery
 * @version 1
 */

public interface SysLogMapper extends BaseMapper<SysLog> {

    public int insertSysLog(@Param("sysLog") SysLog sysLog);

}
