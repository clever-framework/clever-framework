package io.github.toquery.framework.system.service.impl;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysArea;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.repository.SysAreaRepository;
import io.github.toquery.framework.system.repository.SysRoleRepository;
import io.github.toquery.framework.system.service.ISysAreaService;
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
public class SysAreaServiceImpl extends AppBaseServiceImpl<Long, SysArea, SysAreaRepository> implements ISysAreaService {

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


}
