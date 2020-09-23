package io.github.toquery.framework.security.auditor;

import io.github.toquery.framework.core.security.AppUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

        // FIXME
        if (authentication == null) {
            log.error("审计错误，无法获取用户信息");
            return Optional.of(1L);
        }

        AppUserDetails sysUser = (AppUserDetails) authentication.getPrincipal();
        log.debug("自定义审计，当前操作用户ID {} 用户名 {} ", sysUser.getId(), sysUser.getUsername());
        return Optional.of(sysUser.getId());
        // return Optional.of(1L);
    }
}
