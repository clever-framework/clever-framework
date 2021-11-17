package io.github.toquery.framework.security.handler;

import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
@Slf4j
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException) throws IOException, ServletException {
        String errorMsg = "请求访问 " + httpServletRequest.getRequestURI() + " 接口,没有访问权限";
        log.error(errorMsg);
        authenticationException.printStackTrace();
        ResponseParam responseParam = ResponseParam.builder().message(errorMsg).build();
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, JacksonUtils.object2String(responseParam));
    }
}
