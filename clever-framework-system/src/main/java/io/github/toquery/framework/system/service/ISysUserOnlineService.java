package io.github.toquery.framework.system.service;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.security.userdetails.AppUserDetailService;
import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.entity.SysUserOnline;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public interface ISysUserOnlineService extends AppBaseService<SysUserOnline> {

    /**
     * 生成Token信息
     * @param sysUser
     * @param now
     * @param expires
     * @param device
     * @return
     */
    SysUserOnline issueToken(SysUser sysUser, String device);

    /**
     * 用户登录退出
     * @param id
     */
    void logout(Authentication authentication);

    /**
     * 删除过期的用户token
     */
    void deleteExpires();
}
