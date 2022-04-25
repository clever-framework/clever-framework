package io.github.toquery.framework.security.rest;


import com.google.common.base.Strings;
import io.github.toquery.framework.core.constant.AppEnumRoleModel;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.core.security.userdetails.AppUserDetailService;
import io.github.toquery.framework.security.DelegatingSysUserOnline;
import io.github.toquery.framework.security.exception.AppSecurityException;
import io.github.toquery.framework.security.model.AppUserChangePassword;
import io.github.toquery.framework.security.model.JwtResponse;
import io.github.toquery.framework.security.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.entity.SysUserOnline;
import io.github.toquery.framework.system.service.ISysUserService;
import io.github.toquery.framework.web.controller.AppBaseWebController;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户认证信息
 */
@Slf4j
@RestController
public class JwtAuthenticationRest extends AppBaseWebController {


    private final AppProperties appProperties;
    private final PasswordEncoder passwordEncoder;
    private final AppUserDetailService appUserDetailsService;
    private final DelegatingSysUserOnline sysUserOnline;
    private final ISysUserService sysUserService;

    public JwtAuthenticationRest(JwtEncoder encoder,
                                 AppProperties appProperties,
                                 PasswordEncoder passwordEncoder,
                                 ISysUserService sysUserService,
                                 AppUserDetailService appUserDetailsService,
                                 DelegatingSysUserOnline sysUserOnline,
                                 AppSecurityJwtProperties appSecurityJwtProperties) {
        this.appProperties = appProperties;
        this.sysUserService = sysUserService;
        this.appUserDetailsService = appUserDetailsService;
        this.sysUserOnline = sysUserOnline;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * iss: jwt签发者
     * sub: jwt所面向的用户
     * aud: 接收jwt的一方
     * exp: jwt的过期时间，这个过期时间必须要大于签发时间
     * nbf: 定义在什么时间之前，该jwt都是不可用的.
     * iat: jwt的签发时间
     * jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
     *
     * @param authentication
     * @param device
     * @return
     * @throws AppException
     */
    @Timed(value = "system-login", description = "系统-登录")
    @PostMapping(value = "${app.jwt.path.token:/user/login}")
    public ResponseEntity<?> createAuthenticationToken(Authentication authentication, @RequestParam(required = false, defaultValue = "admin") String device) throws AppException {
        SysUser sysUser = (SysUser) authentication.getPrincipal();
        if (sysUser == null || sysUser.getId() == null) {
            return ResponseEntity.badRequest().body(ResponseBodyWrap.builder().fail().message("用户不存在").build());
        }

        SysUserOnline sysUserOnlineInfo = sysUserOnline.issueToken(sysUser, device);

        // Return the token
        return ResponseEntity.ok(ResponseBodyWrap.builder().content(new JwtResponse(sysUserOnlineInfo.getToken())).build());
    }


/*    @Timed(value = "system-token-refresh", description = "系统-token刷新")
    @GetMapping(value = "${app.jwt.path.refresh:/user/refresh}")
    public ResponseEntity<?> refreshAndGetAuthenticationToken() {
        String token = jwtTokenHandler.getJwtToken();
        String username = jwtTokenHandler.getUsernameFromToken(token);
        AppUserDetails user = (AppUserDetails) appUserDetailsService.loadFullUserByUsername(username);

        if (jwtTokenHandler.canTokenBeRefreshed(token, user.getChangePasswordDateTime())) {
            String refreshedToken = jwtTokenHandler.refreshToken(token);
            return ResponseEntity.ok(new JwtResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }*/


    @Timed(value = "system-user-info", description = "系统-用户信息")
    @RequestMapping(value = "${app.jwt.path.info:/user/info}")
    public ResponseEntity<ResponseBodyWrap<?>> getAuthenticatedUser(Authentication authentication,
                                                                    @RequestParam(required = false) Long roleId,
                                                                    @RequestParam(required = false) String roleModel) throws AppSecurityException {
        SysUser user = (SysUser) appUserDetailsService.loadFullUserByUsername(authentication.getName());

        AppEnumRoleModel defaultRoleModel = appProperties.getRoleModel();
        if (!Strings.isNullOrEmpty(roleModel)) {
            defaultRoleModel = AppEnumRoleModel.valueOf(roleModel);
        }

        if (defaultRoleModel == AppEnumRoleModel.COMPLEX) {
            user.complexRole();
        } else if (defaultRoleModel == AppEnumRoleModel.ISOLATE) {
            user.isolateRole(roleId);
        } else {
            log.warn("未知的角色处理类型");
        }
        return ResponseEntity.ok(ResponseBodyWrap.builder().content(user).build());
    }


    @Timed(value = "system-user-password", description = "系统-用户修改密码")
    @RequestMapping(value = "${app.jwt.path.password:/user/password}")
    public ResponseEntity<ResponseBodyWrap<?>> changePassword(Authentication authentication, @Validated @RequestBody AppUserChangePassword changePassword) throws AppException {
        if (!changePassword.getRawPassword().equals(changePassword.getRawPasswordConfirm())) {
            return ResponseEntity.badRequest().body(ResponseBodyWrap.builder().message("两次密码输入不一致").build());
        }
        UserDetails user = sysUserService.changePassword(authentication.getName(), changePassword.getSourcePassword(), changePassword.getRawPassword());
        return ResponseEntity.ok(ResponseBodyWrap.builder().content(user).build());
    }


    @Timed(value = "system-user-register", description = "系统-用户注册")
    @PostMapping(value = "${app.jwt.path.register:/user/register}")
    public ResponseEntity<ResponseBodyWrap<?>> register(@RequestBody SysUser user) throws AppException {
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user = sysUserService.saveSysUserCheck(user);
        return ResponseEntity.ok(ResponseBodyWrap.builder().content(user).build());
    }

    @Timed(value = "system-logout", description = "系统-退出")
    @RequestMapping(value = "${app.jwt.path.logout:/user/logout}")
    public ResponseEntity<ResponseBodyWrap<?>> userLogout(Authentication authentication) {
        sysUserOnline.logout(authentication);
        return ResponseEntity.ok(ResponseBodyWrap.builder().content("user logout").build());
    }
}
