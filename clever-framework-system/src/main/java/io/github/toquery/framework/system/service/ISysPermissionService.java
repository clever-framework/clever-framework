package io.github.toquery.framework.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.toquery.framework.system.constant.SysUserPermissionEnum;
import io.github.toquery.framework.system.entity.SysPermission;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
public interface ISysPermissionService extends IService<SysPermission> {

    /**
     * 通过用户id获取所有角色菜单信息
     *
     * @param userId 用户id
     * @return 所有角色菜单信息
     */
    List<SysPermission> findByUserId(Long userId);

    List<SysPermission> findWithFullByUserId(Long userId);

    List<SysPermission> findByUserId(Long userId, SysUserPermissionEnum... sysUserPermissionEnums);

    List<SysPermission> findByUserIds(Collection<Long> userIds);

    List<SysPermission> findByUserIds(Collection<Long> userIds, SysUserPermissionEnum... sysUserPermissionEnums);

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

    /**
     * 获取全量的分页数据
     * @param filterParam
     * @param current
     * @param requestPageSize
     * @return
     */
    Page<SysPermission> queryWithRoleAndArea( int current, int requestPageSize);

    SysPermission updateUserPermissionCheck(SysPermission sysUserPermission);

    SysPermission saveUserPermissionCheck(SysPermission sysUserPermission);

    SysPermission detailWithRoleAndArea(Long id);

    void authorize(Long userId, List<SysPermission> sysUserPermissions);

    Collection<SysPermission> findByRoleId(Long roleId);

    Collection<SysPermission> findByAreaId(Long areaId);
}
