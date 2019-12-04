package io.github.toquery.framework.system.service;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.curd.service.AppBaseService;
import io.github.toquery.framework.system.entity.SysUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@Service
public interface ISysUserService extends UserDetailsService, AppBaseService<SysUser, Long> {

    List<SysUser> findByIds(Set<Long> ids);

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
     * @param userName       用户名
     * @param sourcePassword 用户原密码明文
     * @param rawPassword    用户新密码明文
     * @return 用户
     * @throws AppException 修改用户密码失败
     */
    SysUser changePassword(String userName, String sourcePassword, String rawPassword) throws AppException;

    /**
     * 删除用户，并效验
     *
     * @param ids 用户
     */
    void deleteSysUserCheck(Set<Long> ids) throws AppException;
}
