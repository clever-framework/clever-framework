package io.github.toquery.framework.security.jwt;

import io.github.toquery.framework.security.domain.SysRole;
import io.github.toquery.framework.security.domain.SysUser;

import java.util.stream.Collectors;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(SysUser user) {
        JwtUser jwtUser = new JwtUser(
                user.getId(),
                user.getUserName(),
                user.getLoginName(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles(),
                user.getEnabled(),
                user.getLastPasswordResetDate()
        );

        jwtUser.setRoles(user.getRoles().stream().map(SysRole::getCode).collect(Collectors.toSet()));
        return jwtUser;
    }
}
