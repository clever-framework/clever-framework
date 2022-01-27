package io.github.toquery.framework.security.jwt.rest;


import com.google.common.base.Strings;
import io.github.toquery.framework.core.constant.AppEnumRoleModel;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.security.web.AppUserChangePassword;
import io.github.toquery.framework.core.security.userdetails.AppUserDetails;
import io.github.toquery.framework.security.jwt.domain.JwtResponse;
import io.github.toquery.framework.security.jwt.exception.AppSecurityJwtException;
import io.github.toquery.framework.security.jwt.handler.JwtTokenHandler;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.core.security.userdetails.AppUserDetailService;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.service.ISysUserService;
import io.github.toquery.framework.web.controller.AppBaseWebController;
import io.github.toquery.framework.web.domain.ResponseBody;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * 用户认证信息
 */
@Slf4j
@RestController
public class JwtAuthenticationRest extends AppBaseWebController {

    @Resource
    private AppSecurityJwtProperties appSecurityJwtProperties;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenHandler jwtTokenHandler;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private AppProperties appProperties;

    @Resource
    private AppUserDetailService appUserDetailsService;


    @Timed(value = "system-login", description = "系统-登录")
    @PostMapping(value = "${app.jwt.path.token:/user/login}")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Map<String,String> loginMap) throws AppException {
        String userName = this.getRequestValue(loginMap, appSecurityJwtProperties.getParam().getUsername(), "未获取到登录用户名");
        String password = this.getRequestValue(loginMap, appSecurityJwtProperties.getParam().getPassword(), "未获取到登录密码");

        Authentication authentication = authenticate(userName, password);

        // Reload password post-security so we can generate the token
        String token = jwtTokenHandler.generateToken((UserDetails) authentication.getPrincipal());
        // Return the token
        return ResponseEntity.ok(ResponseBody.builder().content(new JwtResponse(token)).build());
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
            throw new AppSecurityJwtException("User is disabled", e);
        } catch (BadCredentialsException e) {
            throw new AppSecurityJwtException("用户名或密码错误", e, HttpStatus.BAD_REQUEST);
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


    @Timed(value = "system-token-refresh", description = "系统-token刷新")
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
    }


    @Timed(value = "system-user-info", description = "系统-用户信息")
    @RequestMapping(value = "${app.jwt.path.info:/user/info}")
    public ResponseEntity<ResponseBody> getAuthenticatedUser(@RequestParam(required = false) Long roleId,
                                                             @RequestParam(required = false) String roleModel) throws AppSecurityJwtException {
        String username = this.getUserName();
        SysUser user = (SysUser) appUserDetailsService.loadFullUserByUsername(username);

        AppEnumRoleModel defaultRoleModel = appProperties.getRoleModel();
        if (!Strings.isNullOrEmpty(roleModel)){
            defaultRoleModel = AppEnumRoleModel.valueOf(roleModel);
        }

        if (defaultRoleModel == AppEnumRoleModel.COMPLEX){
            user.complexRole();
        }else if (defaultRoleModel == AppEnumRoleModel.ISOLATE){
            user.isolateRole(roleId);
        } else {
            log.warn("未知的角色处理类型");
        }
        return ResponseEntity.ok(ResponseBody.builder().content(user).build());
    }


    @Timed(value = "system-user-password", description = "系统-用户修改密码")
    @RequestMapping(value = "${app.jwt.path.password:/user/password}")
    public ResponseEntity<ResponseBody> changePassword(@Validated @RequestBody AppUserChangePassword changePassword) throws AppException {
        if (!changePassword.getRawPassword().equals(changePassword.getRawPasswordConfirm())) {
            return ResponseEntity.badRequest().body(ResponseBody.builder().message("两次密码输入不一致").build());
        }
        String userName = this.getUserName();
        UserDetails user = sysUserService.changePassword(userName, changePassword.getSourcePassword(), changePassword.getRawPassword());
        return ResponseEntity.ok(ResponseBody.builder().content(user).build());
    }


    private String getUserName() throws AppSecurityJwtException {
        String token = jwtTokenHandler.getJwtToken();
        return jwtTokenHandler.getUsernameFromToken(token);
    }

    @Resource
    private PasswordEncoder passwordEncoder;


    @Timed(value = "system-user-register", description = "系统-用户注册")
    @PostMapping(value = "${app.jwt.path.register:/user/register}")
    public ResponseEntity<ResponseBody> register(@RequestBody SysUser user) throws AppException {
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user = sysUserService.saveSysUserCheck(user);
        return ResponseEntity.ok(ResponseBody.builder().content(user).build());
    }

    @Timed(value = "system-logout", description = "系统-退出")
    @RequestMapping(value = "${app.jwt.path.logout:/user/logout}")
    public ResponseEntity<ResponseBody> userLogout() {
        return ResponseEntity.ok(ResponseBody.builder().content("user logout").build());
    }
}
