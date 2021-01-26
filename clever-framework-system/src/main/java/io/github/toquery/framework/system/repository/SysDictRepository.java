package io.github.toquery.framework.system.repository;

import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.system.entity.SysArea;
import io.github.toquery.framework.system.entity.SysDict;

/**
 * @author toquery
 * @version 1
 */
//@RepositoryRestResource(path = "sys-role")
public interface SysDictRepository extends AppJpaBaseRepository<SysDict, Long> {

}
