package io.github.toquery.framework.system.service;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.curd.service.AppBaseService;
import io.github.toquery.framework.system.domain.SysUser;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author toquery
 * @version 1
 */
public interface ISysUserService extends UserDetailsService, AppBaseService<SysUser, Long> {

    /**
     * 保存用户，并效验
     *
     * @param sysUser 用户
     * @return 用户
     */
    SysUser saveSysUserCheck(SysUser sysUser) throws AppException;

    /**
     * 根据用户名，修改用户密码
     *
     * @param userName    用户名
     * @param rawPassword 用户密码明文
     * @return 用户
     * @throws AppException 修改用户密码失败
     */
    SysUser changePassword(String userName, String rawPassword) throws AppException;
}
