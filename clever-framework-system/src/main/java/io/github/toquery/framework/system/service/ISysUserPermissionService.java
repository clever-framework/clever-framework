package io.github.toquery.framework.system.service;

import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.system.entity.SysRoleMenu;
import io.github.toquery.framework.system.entity.SysUserPermission;
import io.github.toquery.framework.system.repository.SysUserPermissionRepository;

import java.util.List;

/**
 * @author toquery
 * @version 1
 */
public interface ISysUserPermissionService extends AppBaseService<SysUserPermission, Long> {

    /**
     * 通过用户id获取所有角色菜单信息
     *
     * @param userId 用户id
     * @return 所有角色菜单信息
     */
    List<SysUserPermission> findByUserId(Long userId);

    /**
     * 通过用户id，判断是否存在记录
     *
     * @param userId 用户id
     * @return true 存在 false 不存在记录
     */
    boolean existsByUserId(Long userId);

    /**
     * 通过角色id，判断是否存在记录
     *
     * @param roleId 角色id
     * @return true 存在 false 不存在记录
     */
    boolean existsByRoleId(Long roleId);

    /**
     * 通过区域id，判断是否存在记录
     *
     * @param areaId 区域
     * @return true 存在 false 不存在记录
     */
    boolean existsByAreaId(Long areaId);
}
