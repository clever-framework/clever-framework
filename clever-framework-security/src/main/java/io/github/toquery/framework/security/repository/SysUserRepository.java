package io.github.toquery.framework.security.repository;

import io.github.toquery.framework.security.domain.SysUser;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author toquery
 * @version 1
 */
@RepositoryRestResource(path = "sys-user")
public interface SysUserRepository extends AppJpaBaseRepository<SysUser, Long> {

    @Query("from SysUser where userName=:username")
    SysUser findByUserName(@Param("username") String username);
}
