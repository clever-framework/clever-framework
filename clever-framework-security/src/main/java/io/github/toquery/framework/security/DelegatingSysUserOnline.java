package io.github.toquery.framework.security;

import com.google.common.base.Strings;
import io.github.toquery.framework.security.properties.AppSecurityAdminProperties;
import io.github.toquery.framework.security.provider.JwtTokenProvider;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.entity.SysUserOnline;
import io.github.toquery.framework.system.service.ISysUserOnlineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@RequiredArgsConstructor
public class DelegatingSysUserOnline {

    private final JwtTokenProvider jwtTokenProvider;
    private final ISysUserOnlineService sysUserOnlineService;
    private final AppSecurityAdminProperties appSecurityJwtProperties;


    public SysUserOnline issueToken(SysUser sysUser, String device) {
        Instant now = Instant.now();
        Instant expires = now.plusSeconds(appSecurityJwtProperties.getExpires());

        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setUserId(sysUser.getId());
        sysUserOnline.setDevice(device);
        sysUserOnline.setIssuerDate(LocalDateTime.ofInstant(now, ZoneId.systemDefault()));
        sysUserOnline.setExpiresDate(LocalDateTime.ofInstant(expires, ZoneId.systemDefault()));
        sysUserOnline.setUsername(sysUser.getUsername());
        sysUserOnline.setNickname(sysUser.getNickname());
        sysUserOnline.preInsert();

        sysUserOnline.setToken(jwtTokenProvider.issueToken(sysUser, sysUserOnline.getId().toString(), "ios"));

        sysUserOnlineService.save(sysUserOnline);
        return sysUserOnline;
    }

    /**
     * 用户登录退出
     */
    public void logout(Authentication authentication) {
        String id = ((Jwt) authentication.getPrincipal()).getId();
        if (Strings.isNullOrEmpty(id)) {
            log.error("退出失败，token id 为空");
            return;
        }
        sysUserOnlineService.removeById(Long.valueOf(id));
    }

    public void deleteExpires() {
    }
}
