package io.github.toquery.framework.security.jwt.rest;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.toquery.framework.security.domain.SysUser;
import io.github.toquery.framework.security.jwt.JwtTokenUtil;
import io.github.toquery.framework.security.jwt.JwtUser;
import io.github.toquery.framework.security.jwt.properties.AppJwtProperties;
import io.github.toquery.framework.security.jwt.service.JwtUserRegister;
import io.github.toquery.framework.web.domain.ResponseParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户授权信息
 */
@RestController
public class AuthorizationRestController {

    @Resource
    private AppJwtProperties appJwtProperties;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtUserRegister jwtUserRegister;

    @RequestMapping(value = "${app.jwt.path.info:/user/info}")
    public ResponseEntity getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(appJwtProperties.getHeader());
        if (Strings.isNullOrEmpty(token)) {
            return ResponseEntity.badRequest().body(ResponseParam.fail("未检测到提交的用户信息"));
        }
        if (token.contains("Bearer ")){
            token = token.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        user.setRoles(Lists.newArrayList("admin", "edit"));
        return ResponseEntity.ok(ResponseParam.success().content(user));
    }

    @PostMapping(value = "${app.jwt.path.register:/user/register}")
    public ResponseEntity register(@RequestBody SysUser user) {
        user = jwtUserRegister.register(user);
        return ResponseEntity.ok(ResponseParam.success().content(user));
    }

}
