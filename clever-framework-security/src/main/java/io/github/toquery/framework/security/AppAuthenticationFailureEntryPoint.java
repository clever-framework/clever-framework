package io.github.toquery.framework.security;

import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败将会在这里处理
 * @author toquery
 */
@Slf4j
public class AppAuthenticationFailureEntryPoint implements AuthenticationEntryPoint{



    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException) throws IOException {
        authenticationException.printStackTrace();

        String message = authenticationException.getMessage();
        String errorMsg = "请求访问 " + httpServletRequest.getRequestURI() + " 接口,没有访问权限";
        log.error(errorMsg);
        ResponseBodyWrap<?> responseParam = ResponseBodyWrap.builder().message(message).build();
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.addHeader("WWW-Authenticate", "Basic");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.getWriter().write(JacksonUtils.object2String(responseParam));
        httpServletResponse.getWriter().flush();
        httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }
}
