package io.github.toquery.framework.system.service.impl;

import com.google.common.collect.Maps;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysRoleMenu;
import io.github.toquery.framework.system.entity.SysUserPermission;
import io.github.toquery.framework.system.repository.SysRoleMenuRepository;
import io.github.toquery.framework.system.repository.SysUserPermissionRepository;
import io.github.toquery.framework.system.service.ISysRoleMenuService;
import io.github.toquery.framework.system.service.ISysUserPermissionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
public class SysUserPermissionServiceImpl extends AppBaseServiceImpl<Long, SysUserPermission, SysUserPermissionRepository> implements ISysUserPermissionService {

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
}
