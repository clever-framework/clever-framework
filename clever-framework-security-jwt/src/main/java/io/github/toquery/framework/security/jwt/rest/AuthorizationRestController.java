package io.github.toquery.framework.security.jwt.rest;

import com.google.common.base.Strings;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.security.jwt.JwtTokenUtil;
import io.github.toquery.framework.security.jwt.exception.AppSecurityJwtException;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.security.system.domain.SysUser;
import io.github.toquery.framework.security.system.domain.dto.ChangePassword;
import io.github.toquery.framework.security.system.service.ISysUserService;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户授权信息
 */
@RestController
public class AuthorizationRestController extends AppBaseWebMvcController {


    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private AppSecurityJwtProperties appJwtProperties;

    @RequestMapping(value = "${app.jwt.path.info:/user/info}")
    public ResponseEntity getAuthenticatedUser() throws AppSecurityJwtException {
        String username = this.getUserName();
        SysUser user = (SysUser) userDetailsService.loadUserByUsername(username);
        user.authorities2Roles();
        return ResponseEntity.ok(ResponseParam.builder().build().content(user));
    }

    @RequestMapping(value = "${app.jwt.path.password:/user/password}")
    public ResponseEntity changePassword(@Validated @RequestBody ChangePassword changePassword) throws AppException {
        if (!changePassword.getRawPassword().equals(changePassword.getRawPasswordConfirm())) {
            return ResponseEntity.badRequest().body(ResponseParam.builder().build().message("两次密码输入不一致"));
        }
        String userName = this.getUserName();
        SysUser user = sysUserService.changePassword(userName, changePassword);
        return ResponseEntity.ok(ResponseParam.builder().build().content(user));
    }


    private String getUserName() throws AppSecurityJwtException {
        String token = request.getHeader(appJwtProperties.getHeader());
        if (Strings.isNullOrEmpty(token)) {
            throw new AppSecurityJwtException("未检测到提交的用户信息");
        }
        // 如果协议中以 Bearer 开始，则去获取真正的token
        if (token.contains("Bearer ")) {
            token = token.substring(7);
        }
        return jwtTokenUtil.getUsernameFromToken(token);
    }


    @PostMapping(value = "${app.jwt.path.register:/user/register}")
    public ResponseEntity register(@RequestBody SysUser user) throws AppException {
        user = sysUserService.saveSysUserCheck(user);
        return ResponseEntity.ok(ResponseParam.builder().build().content(user));
    }

    @RequestMapping(value = "${app.jwt.path.logout:/user/logout}")
    public ResponseEntity userLogout() {
        return ResponseEntity.ok(ResponseParam.builder().build().content("user logout"));
    }

}
