package io.github.toquery.framework.security.repository;

import io.github.toquery.framework.security.domain.SysRole;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author toquery
 * @version 1
 */
@RepositoryRestResource(path = "sys-role")
public interface SysRoleRepository extends AppJpaBaseRepository<SysRole, Long> {

}
