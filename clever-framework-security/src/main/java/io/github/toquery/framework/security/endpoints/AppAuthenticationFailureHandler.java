package io.github.toquery.framework.security.endpoints;

import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.github.toquery.framework.web.domain.ResponseBodyWrapBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AppAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ResponseBodyWrapBuilder responseBodyWrapBuilder = ResponseBodyWrap.builder().message(exception.getMessage());

        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            responseBodyWrapBuilder.message("用户信息错误");
        }

        response.getWriter().write(JacksonUtils.object2String(responseBodyWrapBuilder.build()));
        // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, JacksonUtils.object2String(responseParam));
    }
}
