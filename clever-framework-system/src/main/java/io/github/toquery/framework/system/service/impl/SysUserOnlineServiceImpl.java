package io.github.toquery.framework.system.service.impl;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.entity.SysUserOnline;
import io.github.toquery.framework.system.repository.SysUserOnlineRepository;
import io.github.toquery.framework.system.service.ISysUserOnlineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class SysUserOnlineServiceImpl extends AppBaseServiceImpl<SysUserOnline, SysUserOnlineRepository> implements ISysUserOnlineService {


    @Override
    public Map<String, String> getQueryExpressions() {
        Map<String, String> map = new HashMap<>();
        return map;
    }


    @Override
    public SysUserOnline issueToken(SysUser sysUser, String device) {

        Instant now = Instant.now();
        Instant expires = now.plusSeconds(3600L);

        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setUserId(sysUser.getId());
        sysUserOnline.setDevice(device);
        sysUserOnline.setIssuerDate(LocalDateTime.ofInstant(now, ZoneId.systemDefault()));
        sysUserOnline.setExpiresDate(LocalDateTime.ofInstant(expires, ZoneId.systemDefault()));
        sysUserOnline.setUsername(sysUser.getUsername());
        sysUserOnline.setNickname(sysUser.getNickname());
        return super.save(sysUserOnline);
    }

    @Override
    public void logout(Authentication authentication) {
        throw new AppException("未实现退出功能");
    }


    @Override
    public void deleteExpires() {
        Instant expiresDate = Instant.now();
        super.repository.deleteByExpiresDate(expiresDate);
    }
}
