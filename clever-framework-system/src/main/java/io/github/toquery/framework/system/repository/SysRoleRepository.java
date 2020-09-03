package io.github.toquery.framework.system.repository;

import io.github.toquery.framework.dao.annotation.MybatisQuery;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.system.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author toquery
 * @version 1
 */
//@RepositoryRestResource(path = "sys-role")
public interface SysRoleRepository extends AppJpaBaseRepository<SysRole, Long> {

    @MybatisQuery
    List<SysRole> findByCodeOrName(@Param("code") String code, @Param("name")String name);
}
