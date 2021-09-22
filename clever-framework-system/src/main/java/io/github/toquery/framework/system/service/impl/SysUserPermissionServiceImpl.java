package io.github.toquery.framework.system.service.impl;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.constant.SysUserPermissionEnum;
import io.github.toquery.framework.system.entity.SysArea;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.entity.SysUserPermission;
import io.github.toquery.framework.system.repository.SysUserPermissionRepository;
import io.github.toquery.framework.system.service.ISysAreaService;
import io.github.toquery.framework.system.service.ISysRoleService;
import io.github.toquery.framework.system.service.ISysUserPermissionService;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author toquery
 * @version 1
 */
public class SysUserPermissionServiceImpl extends AppBaseServiceImpl<Long, SysUserPermission, SysUserPermissionRepository> implements ISysUserPermissionService {

    @Resource
    private ISysRoleService sysRoleService;

    @Resource
    private ISysAreaService sysAreaService;

    @Override
    public Map<String, String> getQueryExpressions() {
        Map<String, String> map = new HashMap<>();
        map.put("menuId", "menuId:EQ");
        map.put("roleId", "roleId:EQ");
        map.put("areaId", "areaId:EQ");
        return map;
    }

    @Override
    public List<SysUserPermission> findByUserId(Long userId) {
        return super.dao.findByUserId(userId);
    }

    @Override
    public List<SysUserPermission> findByUserId(Long userId, SysUserPermissionEnum... sysUserPermissionEnums) {
        List<SysUserPermission> sysUserPermissions = this.findByUserId(userId);
        if (sysUserPermissionEnums != null && sysUserPermissionEnums.length >= 1) {
            Stream<SysUserPermissionEnum> sysUserPermissionEnumStream = Arrays.stream(sysUserPermissionEnums);
            if (sysUserPermissionEnumStream.anyMatch(item -> item == SysUserPermissionEnum.AREA)) {
                Set<Long> sysAreaIds = sysUserPermissions.stream().map(SysUserPermission::getAreaId).collect(Collectors.toSet());
                Map<Long, SysArea> sysAreaMap = sysAreaService.findByIds(sysAreaIds).stream().collect(Collectors.toMap(SysArea::getId, item -> item, (n, o) -> n));
            }
            if (sysUserPermissionEnumStream.anyMatch(item -> item == SysUserPermissionEnum.ROLE)) {
                Set<Long> sysRoleIds = sysUserPermissions.stream().map(SysUserPermission::getRoleId).collect(Collectors.toSet());
                Map<Long, SysRole> sysRoleMap = sysRoleService.findWithMenuByIds(sysRoleIds).stream().collect(Collectors.toMap(SysRole::getId, item -> item, (n, o) -> n));
            }
        }
        return null;
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
        Page<SysUserPermission> permissionPage = super.queryByPage(filterParam, current, requestPageSize);
        Set<Long> roleIds = permissionPage.getContent().stream().map(SysUserPermission::getRoleId).collect(Collectors.toSet());
        Map<Long, SysRole> roleMap = sysRoleService.findByIds(roleIds).stream().collect(Collectors.toMap(SysRole::getId, item -> item, (n, o) -> n));

        Set<Long> areaIds = permissionPage.getContent().stream().map(SysUserPermission::getAreaId).collect(Collectors.toSet());
        Map<Long, SysArea> areaMap = sysAreaService.findByIds(areaIds).stream().collect(Collectors.toMap(SysArea::getId, item -> item, (n, o) -> n));

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
}
