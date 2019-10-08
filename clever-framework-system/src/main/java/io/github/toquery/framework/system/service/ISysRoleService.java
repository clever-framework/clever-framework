package io.github.toquery.framework.system.service;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.curd.service.AppBaseService;
import io.github.toquery.framework.system.entity.SysRole;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public interface ISysRoleService extends AppBaseService<SysRole, Long> {
    List<SysRole> findByCode(String code);

    List<SysRole> findByName(String name);

    SysRole saveSysRoleCheck(SysRole sysRole) throws AppException;

    void deleteSysRoleCheck(Set<Long> ids) throws AppException;

    SysRole updateSysRoleCheck(SysRole sysRole, HashSet<String> newHashSet) throws AppException;
}
