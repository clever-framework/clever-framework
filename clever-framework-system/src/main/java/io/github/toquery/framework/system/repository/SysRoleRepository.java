package io.github.toquery.framework.system.repository;

import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.system.domain.SysRole;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author toquery
 * @version 1
 */
//@RepositoryRestResource(path = "sys-role")
public interface SysRoleRepository extends AppJpaBaseRepository<SysRole, Long> {

}
