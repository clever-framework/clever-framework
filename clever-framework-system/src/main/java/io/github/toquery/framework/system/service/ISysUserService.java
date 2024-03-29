package io.github.toquery.framework.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.security.userdetails.AppUserDetailService;
import io.github.toquery.framework.system.entity.SysUser;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public interface ISysUserService extends AppUserDetailService, IService<SysUser> {

    List<SysUser> findByIds(Set<Long> ids);


    Page<SysUser> pageWithRole(int requestCurrent, int requestPageSize);

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

    SysUser getByIdWithRole(Long id);


    SysUser getByUsername(String username);
}
