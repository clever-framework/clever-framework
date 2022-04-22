package io.github.toquery.framework.security.auditor;

import io.github.toquery.framework.core.security.AppSecurityKey;
import io.github.toquery.framework.core.security.userdetails.AppUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class AppAuditorAwareImpl implements AuditorAware<Long> {


    public AppAuditorAwareImpl() {
        log.info("初始化 App Auditor Aware 自定义审计");
    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = 1L;
        if (authentication == null) {
            log.error("审计错误，无法获取用户信息");
        } else if (authentication.getPrincipal() instanceof AppUserDetails) {
            AppUserDetails sysUser = (AppUserDetails) authentication.getPrincipal();
            log.debug("自定义审计 AppUserDetails ，当前操作用户ID {} 用户名 {} ", sysUser.getId(), authentication.getName());
            userId = sysUser.getId();
        } else if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            userId = Long.parseLong(jwt.getClaims().get(AppSecurityKey.USERID).toString());
            log.debug("自定义审计 Jwt ，当前操作用户ID {} 用户名 {} ", userId , authentication.getName());
        }
        return Optional.of(userId);
    }
}
