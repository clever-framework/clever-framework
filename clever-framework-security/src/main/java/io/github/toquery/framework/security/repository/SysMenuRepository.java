package io.github.toquery.framework.security.repository;

import io.github.toquery.framework.security.domain.SysMenu;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author toquery
 * @version 1
 */
@RepositoryRestResource(path = "sys-menu")
public interface SysMenuRepository extends AppJpaBaseRepository<SysMenu, Long> {

}
