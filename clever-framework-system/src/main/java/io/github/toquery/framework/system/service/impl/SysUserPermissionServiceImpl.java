package io.github.toquery.framework.system.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.constant.SysUserPermissionEnum;
import io.github.toquery.framework.system.entity.SysArea;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.entity.SysUserPermission;
import io.github.toquery.framework.system.mapper.SysUserPermissionMapper;
import io.github.toquery.framework.system.repository.SysUserPermissionRepository;
import io.github.toquery.framework.system.service.ISysAreaService;
import io.github.toquery.framework.system.service.ISysRoleService;
import io.github.toquery.framework.system.service.ISysUserPermissionService;
import io.github.toquery.framework.system.service.ISysUserService;
import org.springframework.data.domain.Page;

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
public class SysUserPermissionServiceImpl extends AppBaseServiceImpl<SysUserPermission, SysUserPermissionRepository> implements ISysUserPermissionService {

    private final ISysUserService sysUserService;

    private final ISysRoleService sysRoleService;

    private final ISysAreaService sysAreaService;

    private final SysUserPermissionMapper sysUserPermissionMapper;

    public SysUserPermissionServiceImpl(ISysUserService sysUserService, ISysRoleService sysRoleService, ISysAreaService sysAreaService, SysUserPermissionMapper sysUserPermissionMapper) {
        this.sysUserService = sysUserService;
        this.sysRoleService = sysRoleService;
        this.sysAreaService = sysAreaService;
        this.sysUserPermissionMapper = sysUserPermissionMapper;
    }

    @Override
    public Map<String, String> getQueryExpressions() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "userId:EQ");
        map.put("menuId", "menuId:EQ");
        map.put("roleId", "roleId:EQ");
        map.put("areaId", "areaId:EQ");
        map.put("userIds", "userId:IN");
        map.put("menuIds", "menuId:IN");
        map.put("roleIds", "roleId:IN");
        map.put("areaIds", "areaId:IN");
        return map;
    }

    @Override
    public List<SysUserPermission> findByUserId(Long userId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("userId", userId);
        return super.list(param);
    }

    @Override
    public List<SysUserPermission> findWithFullByUserId(Long userId) {
        return sysUserPermissionMapper.findWithFullByUserId(userId);
    }


    @Override
    public List<SysUserPermission> findByUserIds(Collection<Long> userIds) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("userIds", userIds);
        return super.list(param);
    }

    @Override
    public List<SysUserPermission> findByUserId(Long userId, SysUserPermissionEnum... sysUserPermissionEnums) {
        return this.findByUserIds(Lists.newArrayList(userId), sysUserPermissionEnums);
    }

    @Override
    public List<SysUserPermission> findByUserIds(Collection<Long> userIds, SysUserPermissionEnum... sysUserPermissionEnums) {
        List<SysUserPermission> sysUserPermissions = this.findByUserIds(userIds);

        if (sysUserPermissionEnums == null || sysUserPermissionEnums.length < 1) {
            return sysUserPermissions;
        }
        List<SysUserPermissionEnum> sysUserPermissionEnumStream = Arrays.asList(sysUserPermissionEnums);

        Map<Long, SysUser> sysUserMap = new HashMap<>();
        Map<Long, SysArea> sysAreaMap = new HashMap<>();
        Map<Long, SysRole> sysRoleMap = new HashMap<>();

        if (sysUserPermissionEnumStream.contains(SysUserPermissionEnum.USER)) {
            Set<Long> sysUserIds = sysUserPermissions.stream().map(SysUserPermission::getUserId).collect(Collectors.toSet());
            sysUserMap = sysUserService.listByIds(sysUserIds).stream().collect(Collectors.toMap(SysUser::getId, item -> item, (n, o) -> n));
        }

        if (sysUserPermissionEnumStream.contains(SysUserPermissionEnum.AREA)) {
            Set<Long> sysAreaIds = sysUserPermissions.stream().map(SysUserPermission::getAreaId).collect(Collectors.toSet());
            sysAreaMap = sysAreaService.listByIds(sysAreaIds).stream().collect(Collectors.toMap(SysArea::getId, item -> item, (n, o) -> n));
        }

        if (sysUserPermissionEnumStream.contains(SysUserPermissionEnum.ROLE)) {
            Set<Long> sysRoleIds = sysUserPermissions.stream().map(SysUserPermission::getRoleId).collect(Collectors.toSet());
            sysRoleMap = sysRoleService.listByIds(sysRoleIds).stream().collect(Collectors.toMap(SysRole::getId, item -> item, (n, o) -> n));
        }

        if (sysUserPermissionEnumStream.contains(SysUserPermissionEnum.ROLE_MENU)) {
            Set<Long> sysRoleIds = sysUserPermissions.stream().map(SysUserPermission::getRoleId).collect(Collectors.toSet());
            sysRoleMap = sysRoleService.findWithMenuByIds(sysRoleIds).stream().collect(Collectors.toMap(SysRole::getId, item -> item, (n, o) -> n));
        }

        for (SysUserPermission sysUserPermission : sysUserPermissions) {
            sysUserPermission.setUser(sysUserMap.get(sysUserPermission.getUserId()));
            sysUserPermission.setArea(sysAreaMap.get(sysUserPermission.getAreaId()));
            sysUserPermission.setRole(sysRoleMap.get(sysUserPermission.getRoleId()));
        }

        return sysUserPermissions;
    }

    @Override
    public boolean existsByUserId(Long userId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("userId", userId);
        return super.exists(param);
    }

    @Override
    public boolean existsByRoleId(Long roleId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("roleId", roleId);
        return super.exists(param);
    }

    @Override
    public boolean existsByAreaId(Long areaId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("areaId", areaId);
        return super.exists(param);
    }

    @Override
    public Page<SysUserPermission> queryWithRoleAndArea(Map<String, Object> filterParam, int current, int requestPageSize) {
        Page<SysUserPermission> permissionPage = super.page(filterParam, current, requestPageSize);
        Set<Long> roleIds = permissionPage.getContent().stream().map(SysUserPermission::getRoleId).collect(Collectors.toSet());
        Map<Long, SysRole> roleMap = sysRoleService.listByIds(roleIds).stream().collect(Collectors.toMap(SysRole::getId, item -> item, (n, o) -> n));

        Set<Long> areaIds = permissionPage.getContent().stream().map(SysUserPermission::getAreaId).collect(Collectors.toSet());
        Map<Long, SysArea> areaMap = sysAreaService.listByIds(areaIds).stream().collect(Collectors.toMap(SysArea::getId, item -> item, (n, o) -> n));

        permissionPage.forEach(sysUserPermission -> {
            sysUserPermission.setRole(roleMap.get(sysUserPermission.getRoleId()));
            sysUserPermission.setArea(areaMap.get(sysUserPermission.getAreaId()));
        });
        return permissionPage;
    }

    @Override
    public SysUserPermission updateUserPermissionCheck(SysUserPermission sysUserPermission) {
        return super.update(sysUserPermission, Sets.newHashSet("roleId", "areaId"));
    }

    @Override
    public SysUserPermission saveUserPermissionCheck(SysUserPermission sysUserPermission) {
        return super.save(sysUserPermission);
    }

    @Override
    public SysUserPermission detailWithRoleAndArea(Long id) {
        SysUserPermission sysUserPermission = super.getById(id);
        SysRole sysRole = sysRoleService.getById(sysUserPermission.getRoleId());
        sysUserPermission.setRole(sysRole);

        SysArea sysArea = sysAreaService.getById(sysUserPermission.getAreaId());
        sysUserPermission.setArea(sysArea);
        return sysUserPermission;
    }

    @Override
    public void authorize(Long userId, List<SysUserPermission> sysUserPermissions) {
        List<SysUserPermission> dbSysUserPermissionList = this.findByUserId(userId);
        List<Long> dbIds = dbSysUserPermissionList.stream().map(SysUserPermission::getId).filter(Objects::nonNull).collect(Collectors.toList());
        List<SysUserPermission> saveSysUserPermissionList = sysUserPermissions.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
        List<SysUserPermission> updateSysUserPermissionList = Lists.newArrayList(), deleteSysUserPermissionList = Lists.newArrayList();
        sysUserPermissions.forEach(sysUserPermission -> {
            if (dbIds.contains(sysUserPermission.getId())) {
                updateSysUserPermissionList.add(sysUserPermission);
            } else {
                deleteSysUserPermissionList.add(sysUserPermission);
            }
        });

        if (saveSysUserPermissionList.size() > 0) {
            super.save(saveSysUserPermissionList);
        }

        if (updateSysUserPermissionList.size() > 0) {
            super.update(updateSysUserPermissionList, Lists.newArrayList("roleId", "areaId"));
        }

        if (deleteSysUserPermissionList.size() > 0) {
            super.delete(deleteSysUserPermissionList);
        }

    }
}
