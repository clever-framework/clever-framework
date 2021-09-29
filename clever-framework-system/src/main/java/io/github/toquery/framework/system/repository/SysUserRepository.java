package io.github.toquery.framework.system.repository;

import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.system.entity.SysUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author toquery
 * @version 1
 */
public interface SysUserRepository extends AppJpaBaseRepository<SysUser> {

    @SuppressWarnings("MybatisMapperMethodInspection")
    @Query("from SysUser where username=:username")
    public SysUser getByUsername(@Param("username") String username);
}
