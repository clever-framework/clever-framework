package io.github.toquery.framework.security.jwt.rest;


import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.security.jwt.JwtTokenUtil;
import io.github.toquery.framework.security.jwt.JwtUser;
import io.github.toquery.framework.security.jwt.exception.AppJwtException;
import io.github.toquery.framework.security.jwt.properties.AppJwtProperties;
import io.github.toquery.framework.security.jwt.service.JwtAuthenticationResponse;
import io.github.toquery.framework.web.domain.ResponseParam;
import io.github.toquery.framework.web.controller.AppBaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 用户认证信息
 */
@RestController
public class AuthenticationRestController extends AppBaseController {

    @Resource
    private AppJwtProperties appJwtProperties;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private UserDetailsService userDetailsService;

    @PostMapping(value = "${app.jwt.path.token:/user/token}")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JSONObject jsonObject) throws AppException {
        String userName = this.getRequestValue(jsonObject, appJwtProperties.getParam().getUserName(), "未获取到登录用户名");
        String password = this.getRequestValue(jsonObject, appJwtProperties.getParam().getPassword(), "未获取到登录密码");

        authenticate(userName, password);

        // Reload password post-security so we can generate the token
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        String token = jwtTokenUtil.generateToken(userDetails);
        // Return the token
        return ResponseEntity.ok(ResponseParam.builder().build().content(new JwtAuthenticationResponse(token)));
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
        String authToken = request.getHeader(appJwtProperties.getHeader());
        String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }


    /**
     * Authenticates the user. If something is wrong, an {@link AppJwtException} will be thrown
     */
    private void authenticate(String username, String password) throws AppJwtException {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AppJwtException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AppJwtException("Bad credentials!", e);
        }
    }
}
