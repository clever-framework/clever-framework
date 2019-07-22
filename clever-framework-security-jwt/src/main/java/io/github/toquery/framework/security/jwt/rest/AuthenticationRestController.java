package io.github.toquery.framework.security.jwt.rest;


import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.security.jwt.JwtTokenUtil;
import io.github.toquery.framework.security.jwt.exception.AppSecurityJwtException;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.security.jwt.domain.JwtResponse;
import io.github.toquery.framework.security.domain.ChangePassword;
import io.github.toquery.framework.system.domain.SysUser;
import io.github.toquery.framework.system.service.ISysUserService;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 用户认证信息
 */
@RestController
public class AuthenticationRestController extends AppBaseWebMvcController {

    @Resource
    private AppSecurityJwtProperties appSecurityJwtProperties;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private ISysUserService sysUserService;


    @Resource
    private AppSecurityJwtProperties appJwtProperties;


    @PostMapping(value = "${app.jwt.path.token:/user/token}")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JSONObject jsonObject) throws AppException {
        String userName = this.getRequestValue(jsonObject, appSecurityJwtProperties.getParam().getUsername(), "未获取到登录用户名");
        String password = this.getRequestValue(jsonObject, appSecurityJwtProperties.getParam().getPassword(), "未获取到登录密码");

        Authentication authentication = authenticate(userName, password);

        // Reload password post-security so we can generate the token
        String token = jwtTokenUtil.generateToken((UserDetails)authentication.getPrincipal());
        // Return the token
        return ResponseEntity.ok(ResponseParam.builder().build().content(new JwtResponse(token)));
    }

    /**
     * Authenticates the user. If something is wrong, an {@link AppSecurityJwtException} will be thrown
     */
    private Authentication authenticate(String username, String password) throws AppSecurityJwtException {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AppSecurityJwtException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AppSecurityJwtException("用户名或密码错误！", e, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 获取请求提交的数据
     *
     * @param jsonObject      post提交的json数据
     * @param requestParamKey 请求参数的key
     * @param errorMessage    错误信息,当不为空时获取不到指定参数将会抛出这个错误
     */

    private String getRequestValue(JSONObject jsonObject, String requestParamKey, String errorMessage) throws AppException {
        String requestValue = null;
        if (jsonObject != null && !Strings.isNullOrEmpty(jsonObject.getString(requestParamKey))) {
            requestValue = jsonObject.getString(requestParamKey);
        } else {
            String[] requestParameterValues = request.getParameterValues(requestParamKey);
            if (requestParameterValues != null && requestParameterValues.length >= 1) {
                requestValue = requestParameterValues[0];
            }
        }

        if (!Strings.isNullOrEmpty(errorMessage) && Strings.isNullOrEmpty(requestValue)) {
            throw new AppException(errorMessage);
        }

        return requestValue;

    }

    @GetMapping(value = "${app.jwt.path.refresh:/user/refresh}")
    public ResponseEntity<?> refreshAndGetAuthenticationToken() {
        String authToken = request.getHeader(appSecurityJwtProperties.getHeader());
        String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        SysUser user = (SysUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }





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
        SysUser user = sysUserService.changePassword(userName, changePassword.getRawPassword());
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
