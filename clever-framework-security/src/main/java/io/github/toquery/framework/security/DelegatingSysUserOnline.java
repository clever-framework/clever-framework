package io.github.toquery.framework.security;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.toquery.framework.core.security.AppSecurityKey;
import io.github.toquery.framework.security.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.security.provider.JwtTokenProvider;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.entity.SysUserOnline;
import io.github.toquery.framework.system.service.ISysUserOnlineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@RequiredArgsConstructor
public class DelegatingSysUserOnline {

    private final JwtTokenProvider jwtTokenProvider;

    private final ISysUserOnlineService sysUserOnlineService;
    private final AppSecurityJwtProperties appSecurityJwtProperties;




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

        sysUserOnline.setToken(jwtTokenProvider.issueToken(sysUser.getId().toString(), "ios"));

        return sysUserOnlineService.save(sysUserOnline);
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
        sysUserOnlineService.deleteById(Long.valueOf(id));
    }

    public void deleteExpires() {
        sysUserOnlineService.deleteExpires();
    }
}
