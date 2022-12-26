package io.github.toquery.framework.system.rest;


import com.google.common.base.Strings;
import io.github.toquery.framework.core.constant.AppEnumRoleModel;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.core.security.userdetails.AppUserDetailService;
import io.github.toquery.framework.system.DelegatingSysUserOnline;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.entity.SysUserOnline;
import io.github.toquery.framework.system.model.request.UserChangePasswordRequest;
import io.github.toquery.framework.system.model.request.UserLoginRequest;
import io.github.toquery.framework.system.model.respose.JwtResponse;
import io.github.toquery.framework.system.service.ISysUserService;
import io.github.toquery.framework.web.controller.AppBaseWebController;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户认证信息
 */

@RequiredArgsConstructor
@Slf4j
@RestController
public class AuthenticationRest extends AppBaseWebController {

    private final AppProperties appProperties;
    private final ISysUserService sysUserService;
    private final DelegatingSysUserOnline sysUserOnline;
    private final AppUserDetailService appUserDetailsService;
    private final AuthenticationManager authenticationManager;

    /**
     * iss: jwt签发者
     * sub: jwt所面向的用户
     * aud: 接收jwt的一方
     * exp: jwt的过期时间，这个过期时间必须要大于签发时间
     * nbf: 定义在什么时间之前，该jwt都是不可用的.
     * iat: jwt的签发时间
     * jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
     */
    @Timed(value = "system-login", description = "系统-登录")
    @PostMapping(value = "${app.jwt.path.token:/admin/login}")
    public ResponseEntity<?> createAuthenticationToken(@RequestParam(required = false, defaultValue = "admin") String device, @RequestBody UserLoginRequest userLoginRequest) throws AppException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword()));
        SysUserOnline sysUserOnlineInfo = sysUserOnline.issueToken((SysUser) authentication.getPrincipal(), device);
        // Return the token
        return ResponseEntity.ok(ResponseBodyWrap.builder().content(new JwtResponse(sysUserOnlineInfo.getToken())).build());
    }


/*    @Timed(value = "system-token-refresh", description = "系统-token刷新")
    @GetMapping(value = "${app.security.admin.path.refresh:/user/refresh}")
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
    @RequestMapping(value = "${app.security.admin.path.info:/admin/info}")
    public ResponseEntity<ResponseBodyWrap<?>> getAuthenticatedUser(Authentication authentication,
                                                                    @RequestParam(required = false) Long roleId,
                                                                    @RequestParam(required = false) String roleModel) {
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
    @RequestMapping(value = "${app.security.admin.path.password:/admin/password}")
    public ResponseEntity<ResponseBodyWrap<?>> changePassword(Authentication authentication, @Validated @RequestBody UserChangePasswordRequest changePassword) throws AppException {
        if (!changePassword.getRawPassword().equals(changePassword.getRawPasswordConfirm())) {
            return ResponseEntity.badRequest().body(ResponseBodyWrap.builder().message("两次密码输入不一致").build());
        }
        UserDetails user = sysUserService.changePassword(authentication.getName(), changePassword.getSourcePassword(), changePassword.getRawPassword());
        return ResponseEntity.ok(ResponseBodyWrap.builder().content(user).build());
    }


    @Timed(value = "system-logout", description = "系统-退出")
    @RequestMapping(value = "${app.security.admin.path.logout:/admin/logout}")
    public ResponseEntity<ResponseBodyWrap<?>> userLogout(Authentication authentication) {
        sysUserOnline.logout(authentication);
        return ResponseEntity.ok(ResponseBodyWrap.builder().success("user logout").build());
    }
}
