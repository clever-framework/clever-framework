package io.github.toquery.framework.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.system.entity.SysRoleMenu;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 通过角色id，判断是否存在记录
     *
     * @param roleId 角色id
     * @return true 存在 false 不存在记录
     */
    boolean existsByRoleId(Long roleId);

    boolean existsByMenuId(Long menuId);

    List<SysRoleMenu> findByRoleId(Long roleId);

    List<SysRoleMenu> findByRoleIds(Set<Long> sysRoleIds);

    List<SysRoleMenu> findByMenuId(Long menuId);

    List<SysRoleMenu> findByMenuIds(Set<Long> sysMenuIds);


    List<SysMenu> findSysMenuByRoleId(Long sysRoleId);

    List<SysMenu> findSysMenuByRoleIds(Set<Long> sysRoleIds);

    List<SysRoleMenu> findWithSysMenuByRoleIds(Set<Long> sysRoleIds);

    List<SysRoleMenu> reSaveMenu(Long roleId, Collection<Long> menuIds);
}
