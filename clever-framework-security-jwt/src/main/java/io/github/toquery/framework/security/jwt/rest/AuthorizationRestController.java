package io.github.toquery.framework.security.jwt.rest;

import com.google.common.base.Strings;
import io.github.toquery.framework.security.system.domain.SysUser;
import io.github.toquery.framework.security.jwt.JwtTokenUtil;
import io.github.toquery.framework.security.jwt.JwtUser;
import io.github.toquery.framework.security.jwt.service.JwtUserRegister;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import io.github.toquery.framework.webmvc.controller.AppBaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户授权信息
 */
@RestController
public class AuthorizationRestController extends AppBaseController {

    @Resource
    private AppSecurityJwtProperties appJwtProperties;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtUserRegister jwtUserRegister;

    @RequestMapping(value = "${app.jwt.path.info:/user/info}")
    public ResponseEntity getAuthenticatedUser() {
        String token = request.getHeader(appJwtProperties.getHeader());
        if (Strings.isNullOrEmpty(token)) {
            return ResponseEntity.badRequest().body(ResponseParam.builder().build().message("未检测到提交的用户信息"));
        }
        // 如果协议中以 Bearer 开始，则去获取真正的token
        if (token.contains("Bearer ")){
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        return ResponseEntity.ok(ResponseParam.builder().build().content(user));
    }

    @PostMapping(value = "${app.jwt.path.register:/user/register}")
    public ResponseEntity register(@RequestBody SysUser user) {
        user = jwtUserRegister.register(user);
        return ResponseEntity.ok(ResponseParam.builder().build().content(user));
    }

    @RequestMapping(value = "${app.jwt.path.logout:/user/logout}")
    public ResponseEntity userLogout() {
        return ResponseEntity.ok(ResponseParam.builder().build().content("user logout"));
    }

}
