package io.github.toquery.framework.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.mapper.SysRoleMapper;
import io.github.toquery.framework.system.service.ISysRoleMenuService;
import io.github.toquery.framework.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    public static final Set<String> UPDATE_FIELD = Sets.newHashSet("roleName");

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;


    @Override
    public List<SysRole> findByRoleName(String roleName) {
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SysRole::getRoleName, roleName);
        return super.list(lambdaQueryWrapper);
    }

    @Override
    public List<SysRole> findWithMenuByIds(Set<Long> sysRoleIds) {
        List<SysRole> sysRoles = super.listByIds(sysRoleIds);
        List<SysMenu> sysMenus = sysRoleMenuService.findSysMenuByRoleIds(sysRoleIds);
        return null;
    }

    @Override
    public SysRole saveSysRoleCheck(SysRole sysRole) throws AppException {
        List<SysRole> sysRoleList = this.findByRoleName(sysRole.getRoleName());
        if (sysRoleList != null && sysRoleList.size() > 0) {
            throw new AppException("保存角色错误，存在相同名称的角色");
        }
        super.save(sysRole);
        sysRoleMenuService.reSaveMenu(sysRole.getId(), sysRole.getMenuIds());
        return sysRole;
    }

    @Override
    public void deleteSysRoleCheck(Set<Long> ids) throws AppException {
        /*
        todo 删除前验证
        Map<String, Object> filter = new HashMap<>();
        filter.put("idIN", ids);

        Optional<SysRole> sysRoleOptional = super.find(filter).stream().filter(item -> "admin".equalsIgnoreCase(item.getCode()) || "root".equalsIgnoreCase(item.getCode())).findAny();

        if (sysRoleOptional.isPresent()) {
            throw new AppException("禁止删除 admin root 角色");
        }
        */
        super.removeByIds(ids);

    }

    @Override
    public SysRole updateSysRoleCheck(SysRole sysRole) throws AppException {
        List<SysRole> sysRoleList = this.findByRoleName(sysRole.getRoleName());
        Optional<SysRole> sysRoleOptional = sysRoleList.stream().filter(item -> !sysRole.getId().equals(item.getId())).findAny();
        if (sysRoleOptional.isPresent()) {
            throw new AppException("已存在相同名称的角色");
        }
        LambdaUpdateWrapper<SysRole> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysRole::getId, sysRole.getId());
        lambdaUpdateWrapper.set(SysRole::getRoleName, sysRole.getRoleName());
        super.update(sysRole, lambdaUpdateWrapper);
        sysRoleMenuService.reSaveMenu(sysRole.getId(), sysRole.getMenuIds());
        return sysRole;
    }

    @Override
    public SysRole getWithMenusById(Long roleId) {
        SysRole sysRole = super.getById(roleId);
        if (sysRole == null) {
            throw new AppException("角色不存在");
        }
        List<SysMenu> sysMenus = sysRoleMenuService.findSysMenuByRoleId(roleId);
        sysRole.setMenus(sysMenus);
        sysRole.setMenuIds(sysMenus.stream().map(SysMenu::getId).collect(Collectors.toList()));
        return sysRole;
    }
}
