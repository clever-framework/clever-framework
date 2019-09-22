package io.github.toquery.framework.system.service.impl;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.repository.SysRoleRepository;
import io.github.toquery.framework.system.service.ISysRoleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public class SysRoleServiceImpl extends AppBaseServiceImpl<Long, SysRole, SysRoleRepository> implements ISysRoleService {

    @Override
    public Map<String, String> getQueryExpressions() {
        Map<String, String> map = new HashMap<>();
        map.put("idIN", "id:IN");
        map.put("name", "name:EQ");
        map.put("nameLike", "name:LIKE");

        map.put("code", "code:EQ");
        map.put("codeLike", "code:LIKE");
        return map;
    }


    @Override
    public List<SysRole> findByCode(String code) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("code", code);
        return super.find(filter);
    }

    @Override
    public SysRole saveSysRoleCheck(SysRole sysRole) throws AppException {
        List<SysRole> sysRoleList = this.findByCode(sysRole.getCode());
        if (sysRoleList != null && sysRoleList.size() > 0) {
            throw new AppException("保存角色错误，禁止保存 admin root角色");
        }
        return super.save(sysRole);
    }

    @Override
    public void deleteSysRoleCheck(Set<Long> ids) throws AppException {
        Map<String, Object> filter = new HashMap<>();
        filter.put("idIN", ids);
        Optional<SysRole> sysRoleOptional = super.find(filter).stream().filter(item -> "admin".equalsIgnoreCase(item.getCode()) || "root".equalsIgnoreCase(item.getCode())).findAny();
        if (sysRoleOptional.isPresent()) {
            throw new AppException("禁止删除 admin root 角色");
        }
        super.deleteByIds(ids);

    }
}
