package io.github.toquery.framework.security.system.service.impl;

import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.security.system.domain.SysRole;
import io.github.toquery.framework.security.system.repository.SysRoleRepository;
import io.github.toquery.framework.security.system.service.ISysRoleService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Service
public class SysRoleServiceImpl extends AppBaseServiceImpl<Long, SysRole, SysRoleRepository> implements ISysRoleService {

    @Override
    public Map<String, String> getQueryExpressions() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "name:EQ");
        map.put("nameLike", "name:LIKE");

        map.put("code", "code:EQ");
        map.put("codeLike", "code:LIKE");
        return map;
    }


}
