package io.github.toquery.framework.system.repository;

import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.system.domain.SysUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author toquery
 * @version 1
 */
public interface SysUserRepository extends AppJpaBaseRepository<SysUser, Long> {

    @SuppressWarnings("MybatisMapperMethodInspection")
    @Query("from SysUser where username=:username")
    public SysUser getByUsername(@Param("username") String username);
}
