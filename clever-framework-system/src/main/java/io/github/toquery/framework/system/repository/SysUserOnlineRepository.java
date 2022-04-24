package io.github.toquery.framework.system.repository;

import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.entity.SysUserOnline;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

/**
 * @author toquery
 * @version 1
 */
public interface SysUserOnlineRepository extends AppJpaBaseRepository<SysUserOnline> {


    @Modifying
    @Query("delete from SysUserOnline where expiresDate < expiresDate")
    void deleteByExpiresDate(Instant expiresDate);
}
