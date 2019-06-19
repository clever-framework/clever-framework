package io.github.toquery.framework.security.system.service.impl;

import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.security.system.domain.SysUser;
import io.github.toquery.framework.security.system.repository.SysUserRepository;
import io.github.toquery.framework.security.system.service.ISysUserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Service
public class SysUserServiceImpl extends AppBaseServiceImpl<Long, SysUser, SysUserRepository> implements ISysUserService {

    @Override
    public Map<String, String> getQueryExpressions() {
        Map<String, String> map = new HashMap<>();
        map.put("userName","userName:EQ");
        map.put("loginName","loginName:EQ");
        map.put("email","email:EQ");
        map.put("enabled","enabled:EQ");


        map.put("userNameLike","userName:LIKE");
        map.put("loginNameLike","loginName:LIKE");
        map.put("emailLike","email:LIKE");
        return map;
    }

}
