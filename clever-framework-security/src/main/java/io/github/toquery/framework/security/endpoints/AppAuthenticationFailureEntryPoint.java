package io.github.toquery.framework.security.endpoints;

import com.google.common.base.Strings;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败将会在这里处理
 *
 * @author toquery
 */
@Slf4j
@RequiredArgsConstructor
public class AppAuthenticationFailureEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException) throws IOException {
        log.error("AppAuthenticationFailureEntryPoint AuthenticationException = " + authenticationException.getClass().getName(), authenticationException);

        String message = authenticationException.getMessage();
        if (authenticationException instanceof InsufficientAuthenticationException) {
            message = "未获到用户信息，请重新登录！";
        } else if (authenticationException instanceof BadCredentialsException) {
            message = Strings.isNullOrEmpty(message) ? "用户名或密码错误" : message;
        } else {
            log.warn("未处理的认证失败异常类型");
        }

        ResponseBodyWrap<?> responseParam = ResponseBodyWrap.builder().fail(message).build();
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.addHeader("WWW-Authenticate", "Basic");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.getWriter().write(JacksonUtils.object2String(responseParam));
        httpServletResponse.getWriter().flush();
        // httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }
}
