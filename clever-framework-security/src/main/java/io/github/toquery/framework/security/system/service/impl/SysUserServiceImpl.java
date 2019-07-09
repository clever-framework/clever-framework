package io.github.toquery.framework.security.system.service.impl;

import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.security.system.domain.SysUser;
import io.github.toquery.framework.security.system.domain.dto.ChangePassword;
import io.github.toquery.framework.security.system.repository.SysUserRepository;
import io.github.toquery.framework.security.system.service.ISysUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Service
public class SysUserServiceImpl extends AppBaseServiceImpl<Long, SysUser, SysUserRepository> implements ISysUserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> getQueryExpressions() {
        Map<String, String> map = new HashMap<>();
        map.put("userName", "userName:EQ");
        map.put("loginName", "loginName:EQ");
        map.put("email", "email:EQ");
        map.put("enabled", "enabled:EQ");


        map.put("userNameLike", "userName:LIKE");
        map.put("loginNameLike", "loginName:LIKE");
        map.put("emailLike", "email:LIKE");
        return map;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = entityDao.getByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return user;
        }
    }


    @Override
    public SysUser saveSysUserCheck(SysUser sysUser) throws AppException {
        SysUser dbSysUser = entityDao.getByUserName(sysUser.getUsername());
        if (dbSysUser != null) {
            throw new AppException("用户已存在");
        }
        String encodePassword = passwordEncoder.encode(sysUser.getPassword());
        sysUser.setPassword(encodePassword);
        return save(sysUser);
    }

    /**
     * 根据用户名，修改用户密码
     *
     * @param userName       用户名
     * @param changePassword 用户密码
     * @return 用户
     * @throws AppException 修改用户密码失败
     */
    @Override
    public SysUser changePassword(String userName, ChangePassword changePassword) throws AppException {
        SysUser sysUser = entityDao.getByUserName(userName);
        if (sysUser == null) {
            throw new AppException("未找到用户");
        }
        boolean flag = passwordEncoder.matches(changePassword.getSourcePassword(), sysUser.getPassword());
        if (!flag) {
            throw new AppException("用户原密码错误！");
        }

        String encodePassword = passwordEncoder.encode(changePassword.getRawPassword());
        sysUser.setPassword(encodePassword);
        return this.update(sysUser, Sets.newHashSet("password"));
    }
}
