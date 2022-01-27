package io.github.toquery.framework.webmvc.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toquery.framework.webmvc.annotation.ResponseIgnoreWrap;
import io.github.toquery.framework.web.domain.ResponseBody;
import io.github.toquery.framework.webmvc.properties.AppWebMvcProperties;
import io.github.toquery.framework.webmvc.properties.AppWebMvcWrapProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import java.util.Objects;

/**
 *
 */
@Slf4j
@RestControllerAdvice
public class AppResponseWrapAdvice implements ResponseBodyAdvice<Object> {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private AppWebMvcProperties appWebMvcProperties;

    public AppResponseWrapAdvice() {
        log.debug("AppResponseWrapAdvice init");
    }


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.debug("AppResponseWrapAdvice supports returnType:{} converterType {}", returnType, converterType);

        AppWebMvcWrapProperties wrap = appWebMvcProperties.getWrap();


        Class<?> currentClass = returnType.getContainingClass();
        Class<?> classReturnType = Objects.requireNonNull(returnType.getMethod()).getReturnType();
        String methodReturnTypeName = classReturnType.getName();
        log.debug("App 响应 Wrap 正在判断 {} : {} : {}", currentClass.getName(), returnType.getMethod().getName(), methodReturnTypeName);

        //异常处理类注解
        ControllerAdvice controllerAdvice = currentClass.getAnnotation(ControllerAdvice.class);
        RestControllerAdvice restControllerAdvice = currentClass.getAnnotation(RestControllerAdvice.class);
        // 异常处理类不包裹
        if (controllerAdvice != null || restControllerAdvice != null) {
            return false;
        }

        // 只处理 restcontroller 或 responsebody 的方法、类
        RestController restControllerAnnotation = currentClass.getAnnotation(RestController.class);
        org.springframework.web.bind.annotation.ResponseBody responseBodyAnnotation = currentClass.getAnnotation(org.springframework.web.bind.annotation.ResponseBody.class);
        //方法注解
        org.springframework.web.bind.annotation.ResponseBody responseBodyMethodAnnotation = returnType.getMethodAnnotation(org.springframework.web.bind.annotation.ResponseBody.class);

        if (restControllerAnnotation == null && responseBodyAnnotation == null && responseBodyMethodAnnotation == null) {
            log.debug("App 响应 Wrap 只处理 RestController 或 ResponseBody 的方法、类");
            return false;
        }

        if ("void".equalsIgnoreCase(methodReturnTypeName) && !wrap.isVoidObject()) {
            log.debug("App 响应 Wrap 方法返回类型为 void 不包裹");
            return false;
        }

        //类注解
        ResponseIgnoreWrap classAnnotation = currentClass.getAnnotation(ResponseIgnoreWrap.class);
        //方法注解
        ResponseIgnoreWrap ignoreResponseWarp = returnType.getMethodAnnotation(ResponseIgnoreWrap.class);
        if (classAnnotation != null || ignoreResponseWarp != null) {
            // 如果类注解或方法注解是忽略注解，则不进行处理
            return false;
        }
        return wrap.isEnable();
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.debug("AppResponseWrapAdvice beforeBodyWrite body:{} returnType:{} selectedContentType:{} selectedConverterType:{} request:{} response:{}", body, returnType, selectedContentType, selectedConverterType, request, response);

        AppWebMvcWrapProperties wrap = appWebMvcProperties.getWrap();

        Class<?> currentClass = returnType.getContainingClass();
        Class<?> classReturnType = Objects.requireNonNull(returnType.getMethod()).getReturnType();
        String methodReturnTypeName = classReturnType.getName();

        log.debug("App 响应 Wrap 正在处理 {} : {} : {}", currentClass.getName(), returnType.getMethod().getName(), methodReturnTypeName);

        Object result = null;

        if (body == null) {
            if ("void".equalsIgnoreCase(methodReturnTypeName) && wrap.isVoidObject()) {
                result = ResponseBody.builder().build();
            }
        } else {
            if (body instanceof ResponseBody) {
                result = body;
            } else {
                ResponseBody responseResult = ResponseBody.builder().content(body).build();
                if (selectedContentType.isCompatibleWith(new MediaType("text", "*"))) {
//                    HttpHeaders httpHeaders = response.getHeaders();
//                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//                    result = objectMapper.writeValueAsString(responseResult);

                    log.warn("App 响应 Wrap 对象为 text/* 类型，不进行处理");
                    result = body;
                } else {
                    result = responseResult;
                }
            }
        }
        return result;
    }
}
