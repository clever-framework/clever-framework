package io.github.toquery.framework.system.service.impl;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.entity.SysRoleMenu;
import io.github.toquery.framework.system.repository.SysRoleMenuRepository;
import io.github.toquery.framework.system.repository.SysRoleRepository;
import io.github.toquery.framework.system.service.ISysRoleMenuService;
import io.github.toquery.framework.system.service.ISysRoleService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public class SysRoleMenuServiceImpl extends AppBaseServiceImpl<Long, SysRoleMenu, SysRoleMenuRepository> implements ISysRoleMenuService {

    @Override
    public Map<String, String> getQueryExpressions() {
        Map<String, String> map = new HashMap<>();

        map.put("menuId", "menuId:EQ");
        map.put("code", "code:EQ");
        map.put("code", "code:EQ");
        map.put("codeLike", "code:LIKE");
        return map;
    }

    @Override
    public List<SysRoleMenu> findByUserId(Long userId) {
        return null;
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return false;
    }

    @Override
    public boolean existsByRoleId(Long roleId) {
        return false;
    }

    @Override
    public boolean existsByAreaId(Long areaId) {
        return false;
    }
}
