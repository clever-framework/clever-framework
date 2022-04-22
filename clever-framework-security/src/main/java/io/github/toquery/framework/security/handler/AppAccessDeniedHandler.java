package io.github.toquery.framework.security.handler;

import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 用来解决匿名用户访问无权限资源时的异常
 */
@Slf4j
public class AppAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String errorMsg = "请求访问 " + request.getRequestURI() + " 接口,没有访问权限";
        log.error(errorMsg);
        accessDeniedException.printStackTrace();
        ResponseBodyWrap<?> responseParam = ResponseBodyWrap.builder().message(errorMsg).build();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.sendError(HttpServletResponse.SC_FORBIDDEN, JacksonUtils.object2String(responseParam));
    }
}
