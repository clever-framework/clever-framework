package io.github.toquery.framework.security.rest;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.service.ISysUserService;
import io.github.toquery.framework.web.controller.AppBaseWebController;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RequiredArgsConstructor
@Slf4j
@RestController
public class UserRegisterRest extends AppBaseWebController {


    private final PasswordEncoder passwordEncoder;
    private final ISysUserService sysUserService;

    @Timed(value = "system-user-register", description = "系统-用户注册")
    @PostMapping(value = "${app.jwt.path.register:/user/register}")
    public ResponseEntity<ResponseBodyWrap<?>> register(@RequestBody SysUser user) throws AppException {
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user = sysUserService.saveSysUserCheck(user);
        return ResponseEntity.ok(ResponseBodyWrap.builder().content(user).build());
    }
}
