package io.github.toquery.framework.security.provider;

import io.github.toquery.framework.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

@Slf4j
public class AppAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    public AppAuthenticationProvider() {
        log.info(this.getClass().getSimpleName());
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        return authentication;
    }

    /**
     * 只有Authentication为SmsCodeAuthenticationToken使用此Provider认证
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return ISysUserService.class.isAssignableFrom(authentication);
    }


}
