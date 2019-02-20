package io.github.toquery.framework.security.jwt.rest;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import io.github.toquery.framework.security.jwt.JwtTokenUtil;
import io.github.toquery.framework.security.jwt.JwtUser;
import io.github.toquery.framework.security.jwt.exception.AppJwtException;
import io.github.toquery.framework.security.jwt.properties.AppJwtProperties;
import io.github.toquery.framework.security.jwt.service.JwtAuthenticationResponse;
import io.github.toquery.framework.web.domain.ResponseParam;
import org.springframework.http.HttpStatus;
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
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 用户认证信息
 */
@RestController
public class AuthenticationRestController {

    @Resource
    private AppJwtProperties appJwtProperties;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private UserDetailsService userDetailsService;

    @PostMapping(value = "${app.jwt.path.token:/user/token}")
    public ResponseEntity<?> createAuthenticationToken(HttpServletRequest request, @RequestBody JSONObject jsonObject) throws AppJwtException {
        String userName = jsonObject.getString(appJwtProperties.getParam().getUserName());
        String password = jsonObject.getString(appJwtProperties.getParam().getPassword());
        if (Strings.isNullOrEmpty(userName)) {
            String[] userNames = request.getParameterValues(appJwtProperties.getParam().getUserName());
            if (userNames == null || userNames.length <= 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseParam.fail().message("未配置登录用户名"));
            }
            userName = userNames[0];
        }
        if (Strings.isNullOrEmpty(password)) {
            String[] passwords = request.getParameterValues(appJwtProperties.getParam().getPassword());
            if (passwords == null || passwords.length <= 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseParam.fail().message("未配置登录密码"));
            }
            password = passwords[0];
        }
        authenticate(userName, password);
        // Reload password post-security so we can generate the token
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        String token = jwtTokenUtil.generateToken(userDetails);
        // Return the token
        return ResponseEntity.ok(ResponseParam.success().content(new JwtAuthenticationResponse(token)));
    }

    @GetMapping(value = "${app.jwt.path.refresh:/user/refresh}")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
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
