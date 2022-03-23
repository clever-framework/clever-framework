package io.github.toquery.framework.system.repository;

import io.github.toquery.framework.dao.annotation.MybatisQuery;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.system.entity.SysUserPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author toquery
 * @version 1
 */
//@RepositoryRestResource(path = "sys-role")
public interface SysUserPermissionRepository extends AppJpaBaseRepository<SysUserPermission> {

    @MybatisQuery
    List<SysUserPermission> findWithFullByUserId(@Param("userId") Long userId);
}
