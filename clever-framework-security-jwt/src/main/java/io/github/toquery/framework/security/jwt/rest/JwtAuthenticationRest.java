package io.github.toquery.framework.security.jwt.rest;


import com.google.common.base.Strings;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.security.AppUserChangePassword;
import io.github.toquery.framework.core.security.AppUserDetails;
import io.github.toquery.framework.security.jwt.domain.JwtResponse;
import io.github.toquery.framework.security.jwt.exception.AppSecurityJwtException;
import io.github.toquery.framework.security.jwt.handler.JwtTokenHandler;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.log.service.ISysLogService;
import io.github.toquery.framework.system.service.ISysUserService;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * 用户认证信息
 */
@RestController
public class JwtAuthenticationRest extends AppBaseWebMvcController {

    @Resource
    private AppSecurityJwtProperties appSecurityJwtProperties;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenHandler jwtTokenHandler;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ISysLogService sysLogService;


    @Resource
    private AppSecurityJwtProperties appJwtProperties;


    @PostMapping(value = "${app.jwt.path.token:/user/token}")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Map<String,String> loginMap) throws AppException {
        String userName = this.getRequestValue(loginMap, appSecurityJwtProperties.getParam().getUsername(), "未获取到登录用户名");
        String password = this.getRequestValue(loginMap, appSecurityJwtProperties.getParam().getPassword(), "未获取到登录密码");

        Authentication authentication = authenticate(userName, password);

        // Reload password post-security so we can generate the token
        String token = jwtTokenHandler.generateToken((UserDetails) authentication.getPrincipal());
        sysLogService.insertSysLog(((SysUser) authentication.getPrincipal()).getId(), "系统", "登录成功", null, userName, null);
        // Return the token
        return ResponseEntity.ok(ResponseParam.builder().content(new JwtResponse(token)).build());
    }

    /**
     * Authenticates the user. If something is wrong, an {@link AppSecurityJwtException} will be thrown
     */
    private Authentication authenticate(String username, String password) throws AppSecurityJwtException {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            return authentication;
        } catch (DisabledException e) {
            sysLogService.insertSysLog(null, "系统", "登录失败", null, username, null);
            throw new AppSecurityJwtException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            sysLogService.insertSysLog(null, "系统", "登录失败", null, username + "密码错误", null);
            throw new AppSecurityJwtException("用户名或密码错误！", e, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 获取请求提交的数据
     *
     * @param loginMap      post提交的json数据
     * @param requestParamKey 请求参数的key
     * @param errorMessage    错误信息,当不为空时获取不到指定参数将会抛出这个错误
     */

    private String getRequestValue(Map<String,String> loginMap, String requestParamKey, String errorMessage) throws AppException {
        String requestValue = null;
        if (loginMap != null && !Strings.isNullOrEmpty(loginMap.get(requestParamKey))) {
            requestValue = loginMap.get(requestParamKey);
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
        String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authToken.substring(7);
        String username = jwtTokenHandler.getUsernameFromToken(token);
        AppUserDetails user = (AppUserDetails) userDetailsService.loadUserByUsername(username);

        if (jwtTokenHandler.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenHandler.refreshToken(token);
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
        return ResponseEntity.ok(ResponseParam.builder().content(user).build());
    }

    @RequestMapping(value = "${app.jwt.path.password:/user/password}")
    public ResponseEntity changePassword(@Validated @RequestBody AppUserChangePassword changePassword) throws AppException {
        if (!changePassword.getRawPassword().equals(changePassword.getRawPasswordConfirm())) {
            return ResponseEntity.badRequest().body(ResponseParam.builder().message("两次密码输入不一致").build());
        }
        String userName = this.getUserName();
        UserDetails user = sysUserService.changePassword(userName, changePassword.getSourcePassword(), changePassword.getRawPassword());
        return ResponseEntity.ok(ResponseParam.builder().content(user).build());
    }


    private String getUserName() throws AppSecurityJwtException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Strings.isNullOrEmpty(token)) {
            throw new AppSecurityJwtException("未检测到提交的用户信息");
        }
        // 如果协议中以 Bearer 开始，则去获取真正的token
        if (token.contains("Bearer ")) {
            token = token.substring(7);
        }
        return jwtTokenHandler.getUsernameFromToken(token);
    }

    @Resource
    private PasswordEncoder passwordEncoder;


    @PostMapping(value = "${app.jwt.path.register:/user/register}")
    public ResponseEntity register(@RequestBody SysUser user) throws AppException {
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user = sysUserService.saveSysUserCheck(user);
        return ResponseEntity.ok(ResponseParam.builder().content(user).build());
    }

    @RequestMapping(value = "${app.jwt.path.logout:/user/logout}")
    public ResponseEntity userLogout() {
        return ResponseEntity.ok(ResponseParam.builder().content("user logout").build());
    }
}
