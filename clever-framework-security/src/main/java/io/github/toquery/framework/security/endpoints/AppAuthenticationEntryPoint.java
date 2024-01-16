package io.github.toquery.framework.security.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.github.toquery.framework.web.domain.ResponseBodyWrapBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败将会在这里处理
 *
 * @author toquery
 */
@Slf4j
@RequiredArgsConstructor
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * @see org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint
     * @param httpServletRequest that resulted in an <code>AuthenticationException</code>
     * @param httpServletResponse so that the user agent can begin authentication
     * @param authenticationException that caused the invocation
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException) throws IOException {
        log.error("AppAuthenticationFailureEntryPoint AuthenticationException.message(" + authenticationException.getClass().getName(), authenticationException);

        ResponseBodyWrapBuilder responseBodyWrapBuilder = ResponseBodyWrap.builder().fail(authenticationException.getMessage()).code(401);

        if (authenticationException instanceof InsufficientAuthenticationException) {
            responseBodyWrapBuilder.message("未获到用户信息，请重新登录！");
        } else if (authenticationException instanceof UsernameNotFoundException) { // 账号不存在
            responseBodyWrapBuilder.message("账号不存在");
        } else if (authenticationException instanceof BadCredentialsException) { // 用户名或密码错误
            responseBodyWrapBuilder.message("用户名或密码错误");
        } else if (authenticationException instanceof AccountExpiredException) { // 账号已过期
            responseBodyWrapBuilder.message("账号已过期");
        } else if (authenticationException instanceof LockedException) {  // 账号已被锁定
            responseBodyWrapBuilder.message("账号已被锁定");
        } else if (authenticationException instanceof CredentialsExpiredException) {  // 用户凭证已失效
            responseBodyWrapBuilder.message("用户凭证已失效");
        } else if (authenticationException instanceof DisabledException) {  // 账号已被禁用
            responseBodyWrapBuilder.message("账号已被禁用");
        } else if (authenticationException instanceof AuthenticationServiceException) {
            responseBodyWrapBuilder.message("登录失败");
        } else if (authenticationException instanceof AuthenticationCredentialsNotFoundException) {
            responseBodyWrapBuilder.message("未获取到登录信息，请重新登录！");
        } else if (authenticationException instanceof InvalidBearerTokenException) {
            responseBodyWrapBuilder.message("用户登录信息失效，请重新登录！");
        }else {
            log.warn("未处理的认证失败异常类型");
        }

        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        // httpServletResponse.addHeader("WWW-Authenticate", "Basic");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.getWriter().write(JacksonUtils.object2String(objectMapper, responseBodyWrapBuilder.build()));
        httpServletResponse.getWriter().flush();
        // httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }
}
