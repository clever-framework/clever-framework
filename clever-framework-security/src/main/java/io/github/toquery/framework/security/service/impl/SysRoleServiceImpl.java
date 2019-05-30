package io.github.toquery.framework.security.service.impl;

import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.security.domain.SysRole;
import io.github.toquery.framework.security.repository.SysRoleRepository;
import io.github.toquery.framework.security.service.ISysRoleService;
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
        return map;
    }


}
