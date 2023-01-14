package io.github.toquery.framework.security.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toquery.framework.common.util.AppJacksonUtils;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 用来解决匿名用户访问无权限资源时的异常
 */
@Slf4j
@RequiredArgsConstructor
public class AppAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String errorMsg = "请求访问 " + request.getRequestURI() + " 时没有访问权限";
        log.error(errorMsg + "," + accessDeniedException.getClass().getName(), accessDeniedException);
        ResponseBodyWrap<?> responseParam = ResponseBodyWrap.builder().fail(errorMsg).build();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(AppJacksonUtils.object2String(objectMapper, responseParam));
        response.getWriter().flush();
        // response.sendError(HttpServletResponse.SC_FORBIDDEN, JacksonUtils.object2String(responseParam));
    }
}
