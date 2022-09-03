package io.github.toquery.framework.security.utils;

import io.github.toquery.framework.core.security.AppSecurityKey;
import io.github.toquery.framework.core.security.userdetails.AppUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Web环境下的spring-security工具类
 */
public class AppSecurityUtils {

    /**
     * 获取认证(登录)异常
     */
    public static Exception getAuthenticationException(HttpServletRequest request) {
        String key = WebAttributes.AUTHENTICATION_EXCEPTION;
        Exception exception = (Exception) request.getAttribute(key);
        if (exception == null) {
            exception = (Exception) request.getSession().getAttribute(key);
            if (exception != null) {
                request.getSession().removeAttribute(key);
            }
        }
        return exception;
    }

    /**
     * 获取当前登录身份证明(Authentication)
     */
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    public static String getUserName() {
        return AppSecurityUtils.getCurrentAuthentication().getName();
    }

    @Deprecated
    public static Long getUserId() {
        Long userId = null;
        Authentication authentication = AppSecurityUtils.getCurrentAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof AppUserDetails) {
                userId = ((AppUserDetails) authentication.getPrincipal()).getId();
            } else if (principal instanceof Jwt) {
                userId = ((Jwt) authentication.getPrincipal()).getClaim(AppSecurityKey.USERID);
            }
        }
        return userId;
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static UserDetails getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 执行退出登录
     *
     * @param request
     * @param response
     */
    public static void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }

}
