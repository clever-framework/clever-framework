package io.github.toquery.framework.webmvc.advice;

import io.github.toquery.framework.webmvc.annotation.IgnoreResponseWrap;
import io.github.toquery.framework.webmvc.domain.ResponseResult;
import io.github.toquery.framework.webmvc.properties.AppWebMvcProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 *
 */
@Slf4j
@RestControllerAdvice
public class AppResponseBodyAdvice implements ResponseBodyAdvice<Object> {


    @Resource
    private AppWebMvcProperties appWebMvcProperties;

    public AppResponseBodyAdvice() {
        log.debug("AppResponseBodyAdvice init");
    }


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        boolean ignoreResponseWarp = Arrays.stream(returnType.getMethodAnnotations()).anyMatch(annotation -> annotation.annotationType().equals(IgnoreResponseWrap.class));
        log.debug("AppResponseBodyAdvice supports returnType:{} converterType {}", returnType, converterType);
        return appWebMvcProperties.isAutoWrapResponse() && !ignoreResponseWarp;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.debug("AppResponseBodyAdvice beforeBodyWrite body:{} returnType:{} selectedContentType:{} selectedConverterType:{} request:{} response:{}", body, returnType, selectedContentType, selectedConverterType, request, response);
        if (body instanceof ResponseResult) {
            return body;
        }
        return ResponseResult.builder().content(body);
    }
}
