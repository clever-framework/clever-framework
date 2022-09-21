package io.github.toquery.framework.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import io.github.toquery.framework.system.constant.SysUserPermissionEnum;
import io.github.toquery.framework.system.entity.SysArea;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.entity.SysPermission;
import io.github.toquery.framework.system.mapper.SysPermissionMapper;
import io.github.toquery.framework.system.service.ISysAreaService;
import io.github.toquery.framework.system.service.ISysRoleService;
import io.github.toquery.framework.system.service.ISysPermissionService;
import io.github.toquery.framework.system.service.ISysUserService;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    private final ISysUserService sysUserService;

    private final ISysRoleService sysRoleService;

    private final ISysAreaService sysAreaService;

    private final SysPermissionMapper sysUserPermissionMapper;

    public SysPermissionServiceImpl(ISysUserService sysUserService, ISysRoleService sysRoleService, ISysAreaService sysAreaService, SysPermissionMapper sysUserPermissionMapper) {
        this.sysUserService = sysUserService;
        this.sysRoleService = sysRoleService;
        this.sysAreaService = sysAreaService;
        this.sysUserPermissionMapper = sysUserPermissionMapper;
    }

    @Override
    public List<SysPermission> findByUserId(Long userId) {
        LambdaQueryWrapper<SysPermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysPermission::getUserId, userId);
        return super.list(lambdaQueryWrapper);
    }

    @Override
    public List<SysPermission> findWithFullByUserId(Long userId) {
        return sysUserPermissionMapper.findWithFullByUserId(userId);
    }


    @Override
    public List<SysPermission> findByUserIds(Collection<Long> userIds) {
        LambdaQueryWrapper<SysPermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SysPermission::getUserId, userIds);
        return super.list(lambdaQueryWrapper);
    }

    @Override
    public List<SysPermission> findByUserId(Long userId, SysUserPermissionEnum... sysUserPermissionEnums) {
        return this.findByUserIds(Lists.newArrayList(userId), sysUserPermissionEnums);
    }

    @Override
    public List<SysPermission> findByUserIds(Collection<Long> userIds, SysUserPermissionEnum... sysUserPermissionEnums) {
        List<SysPermission> sysUserPermissions = this.findByUserIds(userIds);

        if (sysUserPermissionEnums == null || sysUserPermissionEnums.length < 1) {
            return sysUserPermissions;
        }
        List<SysUserPermissionEnum> sysUserPermissionEnumStream = Arrays.asList(sysUserPermissionEnums);

        Map<Long, SysUser> sysUserMap = new HashMap<>();
        Map<Long, SysArea> sysAreaMap = new HashMap<>();
        Map<Long, SysRole> sysRoleMap = new HashMap<>();

        if (sysUserPermissionEnumStream.contains(SysUserPermissionEnum.USER)) {
            Set<Long> sysUserIds = sysUserPermissions.stream().map(SysPermission::getUserId).collect(Collectors.toSet());
            sysUserMap = sysUserService.listByIds(sysUserIds).stream().collect(Collectors.toMap(SysUser::getId, item -> item, (n, o) -> n));
        }

        if (sysUserPermissionEnumStream.contains(SysUserPermissionEnum.AREA)) {
            Set<Long> sysAreaIds = sysUserPermissions.stream().map(SysPermission::getAreaId).collect(Collectors.toSet());
            sysAreaMap = sysAreaService.listByIds(sysAreaIds).stream().collect(Collectors.toMap(SysArea::getId, item -> item, (n, o) -> n));
        }

        if (sysUserPermissionEnumStream.contains(SysUserPermissionEnum.ROLE)) {
            Set<Long> sysRoleIds = sysUserPermissions.stream().map(SysPermission::getRoleId).collect(Collectors.toSet());
            sysRoleMap = sysRoleService.listByIds(sysRoleIds).stream().collect(Collectors.toMap(SysRole::getId, item -> item, (n, o) -> n));
        }

        if (sysUserPermissionEnumStream.contains(SysUserPermissionEnum.ROLE_MENU)) {
            Set<Long> sysRoleIds = sysUserPermissions.stream().map(SysPermission::getRoleId).collect(Collectors.toSet());
            sysRoleMap = sysRoleService.findWithMenuByIds(sysRoleIds).stream().collect(Collectors.toMap(SysRole::getId, item -> item, (n, o) -> n));
        }

        for (SysPermission sysUserPermission : sysUserPermissions) {
            sysUserPermission.setUser(sysUserMap.get(sysUserPermission.getUserId()));
            sysUserPermission.setArea(sysAreaMap.get(sysUserPermission.getAreaId()));
            sysUserPermission.setRole(sysRoleMap.get(sysUserPermission.getRoleId()));
        }

        return sysUserPermissions;
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return !this.findByUserId(userId).isEmpty();
    }

    @Override
    public boolean existsByRoleId(Long roleId) {
        return !this.findByRoleId(roleId).isEmpty();
    }

    @Override
    public boolean existsByAreaId(Long areaId) {
        return !this.findByAreaId(areaId).isEmpty();
    }

    @Override
    public Page<SysPermission> queryWithRoleAndArea( int current, int requestPageSize) {
        Page<SysPermission> permissionPage = super.page(new Page<>(current, requestPageSize));
        Set<Long> roleIds = permissionPage.getRecords().stream().map(SysPermission::getRoleId).collect(Collectors.toSet());
        Map<Long, SysRole> roleMap = sysRoleService.listByIds(roleIds).stream().collect(Collectors.toMap(SysRole::getId, item -> item, (n, o) -> n));

        Set<Long> areaIds = permissionPage.getRecords().stream().map(SysPermission::getAreaId).collect(Collectors.toSet());
        Map<Long, SysArea> areaMap = sysAreaService.listByIds(areaIds).stream().collect(Collectors.toMap(SysArea::getId, item -> item, (n, o) -> n));

        permissionPage.getRecords().forEach(sysUserPermission -> {
            sysUserPermission.setRole(roleMap.get(sysUserPermission.getRoleId()));
            sysUserPermission.setArea(areaMap.get(sysUserPermission.getAreaId()));
        });
        return permissionPage;
    }

    @Override
    public SysPermission updateUserPermissionCheck(SysPermission sysUserPermission) {
        LambdaUpdateWrapper<SysPermission> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysPermission::getId, sysUserPermission.getId());
        lambdaUpdateWrapper.set(SysPermission::getAreaId, sysUserPermission.getAreaId());
        lambdaUpdateWrapper.set(SysPermission::getRoleId, sysUserPermission.getRoleId());
        super.update(sysUserPermission, lambdaUpdateWrapper);
        return sysUserPermission;
    }

    @Override
    public SysPermission saveUserPermissionCheck(SysPermission sysUserPermission) {
        super.save(sysUserPermission);
        return sysUserPermission;
    }

    @Override
    public SysPermission detailWithRoleAndArea(Long id) {
        SysPermission sysUserPermission = super.getById(id);
        SysRole sysRole = sysRoleService.getById(sysUserPermission.getRoleId());
        sysUserPermission.setRole(sysRole);

        SysArea sysArea = sysAreaService.getById(sysUserPermission.getAreaId());
        sysUserPermission.setArea(sysArea);
        return sysUserPermission;
    }

    @Override
    public void authorize(Long userId, List<SysPermission> sysUserPermissions) {
        List<SysPermission> dbSysUserPermissionList = this.findByUserId(userId);
        List<Long> dbIds = dbSysUserPermissionList.stream().map(SysPermission::getId).filter(Objects::nonNull).collect(Collectors.toList());
        List<SysPermission> saveSysUserPermissionList = sysUserPermissions.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
        List<SysPermission> updateSysUserPermissionList = Lists.newArrayList(), deleteSysUserPermissionList = Lists.newArrayList();
        sysUserPermissions.forEach(sysUserPermission -> {
            if (dbIds.contains(sysUserPermission.getId())) {
                updateSysUserPermissionList.add(sysUserPermission);
            } else {
                deleteSysUserPermissionList.add(sysUserPermission);
            }
        });

        if (saveSysUserPermissionList.size() > 0) {
            super.saveBatch(saveSysUserPermissionList);
        }

        if (updateSysUserPermissionList.size() > 0) {
            super.updateBatchById(updateSysUserPermissionList);
        }

        if (deleteSysUserPermissionList.size() > 0) {
            super.removeBatchByIds(deleteSysUserPermissionList);
        }

    }

    @Override
    public Collection<SysPermission> findByRoleId(Long roleId) {
        LambdaQueryWrapper<SysPermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SysPermission::getRoleId, roleId);
        return super.list(lambdaQueryWrapper);
    }

    @Override
    public Collection<SysPermission> findByAreaId(Long areaId) {
        LambdaQueryWrapper<SysPermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SysPermission::getAreaId, areaId);
        return super.list(lambdaQueryWrapper);
    }
}
