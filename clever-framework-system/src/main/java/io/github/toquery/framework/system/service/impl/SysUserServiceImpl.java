package io.github.toquery.framework.system.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.repository.SysUserRepository;
import io.github.toquery.framework.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
        map.put("idIN", "id:IN");
        map.put("username", "username:EQ");
        map.put("nickname", "nickname:EQ");
        map.put("email", "email:EQ");
        map.put("status", "status:EQ");
        map.put("usernameLike", "username:LIKE");
        // map.put("loginnameLike", "loginname:LIKE");
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
    public List<SysUser> findByIds(Set<Long> ids) {
        return entityDao.findAllById(ids);
    }

    @Override
    public SysUser saveSysUserCheck(SysUser sysUser) throws AppException {
        String userName = sysUser.getUsername();
        if (Strings.isNullOrEmpty(userName)) {
            throw new AppException("用户名不能为空！");
        } else if (userName.equalsIgnoreCase("admin") || userName.equalsIgnoreCase("root")) {
            throw new AppException("用户名不能为 admin root ！");
        }

        SysUser dbSysUser = entityDao.getByUsername(userName);
        if (dbSysUser != null) {
            throw new AppException("用户已存在");
        }
//        String encodePassword = passwordEncoder.encode(sysUser.getPassword());
//        sysUser.setPassword(encodePassword);
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
    public SysUser changePassword(String userName, String sourcePassword, String rawPassword) throws AppException {
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

    @Override
    public void deleteSysUserCheck(Set<Long> ids) throws AppException {
        Map<String, Object> filter = new HashMap<>();
        filter.put("idIN", ids);
        Optional<SysUser> sysUser = super.find(filter).stream().filter(item -> "admin".equalsIgnoreCase(item.getUsername()) || "root".equalsIgnoreCase(item.getUsername())).findAny();
        if (sysUser.isPresent()) {
            throw new AppException("删除失败，无法删除 admin root 用户！");
        }
        super.deleteByIds(ids);
    }
}
