package io.github.toquery.framework.system.repository;

import io.github.toquery.framework.dao.annotation.MybatisQuery;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.system.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
//@RepositoryRestResource(path = "sys-role")
public interface SysRoleMenuRepository extends AppJpaBaseRepository<SysRoleMenu> {

    @MybatisQuery
    List<SysRoleMenu> findWithSysRoleMenuByRoleIds(@Param("sysRoleIds") Set<Long> sysRoleIds);
}
