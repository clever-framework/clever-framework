package io.github.toquery.framework.system.service;

import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.system.constant.SysUserPermissionEnum;
import io.github.toquery.framework.system.entity.SysRoleMenu;
import io.github.toquery.framework.system.entity.SysUserPermission;
import io.github.toquery.framework.system.repository.SysUserPermissionRepository;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

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

    List<SysUserPermission> findByUserId(Long userId, SysUserPermissionEnum... sysUserPermissionEnums);

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
    Page<SysUserPermission> queryWithRoleAndArea(Map<String, Object> filterParam, int current, int requestPageSize);

    SysUserPermission updateUserPermissionCheck(SysUserPermission sysUserPermission);

    SysUserPermission saveUserPermissionCheck(SysUserPermission sysUserPermission);

    SysUserPermission detailWithRoleAndArea(Long id);
}
