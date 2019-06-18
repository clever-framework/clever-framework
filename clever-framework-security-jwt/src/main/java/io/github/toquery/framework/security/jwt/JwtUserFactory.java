package io.github.toquery.framework.security.jwt;

import io.github.toquery.framework.security.system.domain.SysMenu;
import io.github.toquery.framework.security.system.domain.SysRole;
import io.github.toquery.framework.security.system.domain.SysUser;

import java.util.Set;
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

        Set<String> roleCodes = user.getRoles().stream().map(SysRole::getCode).collect(Collectors.toSet());

        Set<String> menuCodes = user.getRoles().stream().flatMap(item -> item.getMenus().stream().map(SysMenu::getCode)).collect(Collectors.toSet());
        menuCodes.addAll(roleCodes);
        jwtUser.setRoles(menuCodes);
        return jwtUser;
    }
}
