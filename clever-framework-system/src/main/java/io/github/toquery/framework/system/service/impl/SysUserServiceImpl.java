package io.github.toquery.framework.system.service.impl;

import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.domain.SysUser;
import io.github.toquery.framework.system.repository.SysUserRepository;
import io.github.toquery.framework.system.service.ISysUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
public class SysUserServiceImpl extends AppBaseServiceImpl<Long, SysUser, SysUserRepository> implements ISysUserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> getQueryExpressions() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "username:EQ");
        map.put("nickname", "nickname:EQ");
        map.put("email", "email:EQ");
        map.put("status", "status:EQ");


        map.put("usernameLike", "username:LIKE");
        map.put("loginnameLike", "loginname:LIKE");
        map.put("emailLike", "email:LIKE");
        return map;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = entityDao.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return user;
        }
    }


    @Override
    public SysUser saveSysUserCheck(SysUser sysUser) throws AppException {
        SysUser dbSysUser = entityDao.getByUsername(sysUser.getUsername());
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
     * @param userName    用户名
     * @param rawPassword 用户新密码明文
     * @return 用户
     * @throws AppException 修改用户密码失败
     */
    @Override
    public SysUser changePassword(String userName, String sourcePassword,String rawPassword) throws AppException {
        SysUser sysUser = entityDao.getByUsername(userName);
        if (sysUser == null) {
            throw new AppException("未找到用户");
        }
        boolean flag = passwordEncoder.matches(sourcePassword, sysUser.getPassword());
        if (!flag) {
            throw new AppException("用户原密码错误！");
        }
        String encodePassword = passwordEncoder.encode(rawPassword);
        sysUser.setPassword(encodePassword);
        sysUser.setLastPasswordResetDate(new Date());
        return this.update(sysUser, Sets.newHashSet("password", "lastPasswordResetDate"));
    }
}
