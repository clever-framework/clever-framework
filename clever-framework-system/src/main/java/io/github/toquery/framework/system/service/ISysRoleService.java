package io.github.toquery.framework.system.service;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.system.entity.SysRole;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public interface ISysRoleService extends AppBaseService<SysRole> {

    /**
     * 通过角色名称获取角色信息
     *
     * @param roleName 角色名称
     * @return 角色信息
     */
    List<SysRole> findByRoleName(String roleName);

    /**
     * 通过角色IDS获取角色信息
     *
     * @param sysRoleIds 角色IDS
     * @return 角色信息
     */
    List<SysRole> findWithMenuByIds(Set<Long> sysRoleIds);

    SysRole saveSysRoleCheck(SysRole sysRole) throws AppException;

    void deleteSysRoleCheck(Set<Long> ids) throws AppException;

    SysRole updateSysRoleCheck(SysRole sysRole) throws AppException;

    SysRole getWithMenusById(Long roleId);
}
