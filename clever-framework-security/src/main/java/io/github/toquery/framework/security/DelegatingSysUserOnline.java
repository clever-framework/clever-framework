package io.github.toquery.framework.security;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.toquery.framework.core.security.AppSecurityKey;
import io.github.toquery.framework.security.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.entity.SysUserOnline;
import io.github.toquery.framework.system.service.ISysUserOnlineService;
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
public class DelegatingSysUserOnline {

    private final JwtEncoder encoder;
    private final AppSecurityJwtProperties appSecurityJwtProperties;

    private final ISysUserOnlineService sysUserOnlineService;

    public DelegatingSysUserOnline(JwtEncoder encoder,
                                   ISysUserOnlineService sysUserOnlineService,
                                   AppSecurityJwtProperties appSecurityJwtProperties) {
        this.encoder = encoder;
        this.sysUserOnlineService = sysUserOnlineService;
        this.appSecurityJwtProperties = appSecurityJwtProperties;
    }

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


        sysUser.preInsert();
        Long id = sysUserOnline.getUserId();

        String scope = sysUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(id.toString())
                .issuer(appSecurityJwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(expires)
                .subject(sysUser.getUsername())
                .audience(Lists.newArrayList(device))
                .claim(AppSecurityKey.USERID, sysUser.getId().toString())
                .claim(AppSecurityKey.SCOPE, scope)
                .build();

        String token = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        sysUserOnline.setToken(token);

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