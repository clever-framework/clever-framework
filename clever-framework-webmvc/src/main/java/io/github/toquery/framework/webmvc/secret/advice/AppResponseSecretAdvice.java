package io.github.toquery.framework.webmvc.secret.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.github.toquery.framework.common.util.AesCbcUtil;
import io.github.toquery.framework.web.domain.ResponseBody;
import io.github.toquery.framework.webmvc.properties.AppWebMvcProperties;
import io.github.toquery.framework.webmvc.properties.AppWebMvcSecretProperties;
import io.github.toquery.framework.webmvc.utils.AppSecretAdviceUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;

/**
 *
 */
@Slf4j
@RestControllerAdvice
public class AppResponseSecretAdvice implements ResponseBodyAdvice<Object> {

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private AppWebMvcProperties appWebMvcProperties;

    public AppResponseSecretAdvice() {
        log.debug("AppResponseSecretAdvice init");
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        log.debug("AppResponseSecretAdvice supports methodParameter:{} converterType:{}", methodParameter, converterType);

        Class<?> currentClass = methodParameter.getContainingClass();
        log.debug("App 响应加密 正在判断 {} : {} : {}", currentClass.getName(), methodParameter.getMethod().getName(), methodParameter.getMethod().getReturnType().getName());

        AppWebMvcSecretProperties.AppWebMvcSecretItem response = appWebMvcProperties.getSecret().getResponse();

        return AppSecretAdviceUtils.responseNeedEncrypt(response, currentClass, methodParameter);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter parameter, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.debug("AppResponseSecretAdvice beforeBodyWrite body:{} returnType:{} selectedContentType:{} selectedConverterType:{} request:{} response:{}", body, parameter, selectedContentType, selectedConverterType, request, response);

        Class<?> currentClass = parameter.getContainingClass();
        log.debug("App 响应加密 正在处理 {} : {} : {}", currentClass.getName(), parameter.getMethod().getName(), parameter.getMethod().getReturnType().getName());


        if (body == null) {
            log.warn("AppResponseSecretAdvice beforeBodyWrite body is null");
            return null;
        }

        //  text/* 格式处理
        if (selectedContentType.isCompatibleWith(new MediaType("text", "*"))) {
            log.warn("AppResponseSecretAdvice beforeBodyWrite body is text/* 将忽略加密");
            return body;
        }

        AppWebMvcSecretProperties.AppWebMvcSecretItem responseSecretItem = appWebMvcProperties.getSecret().getResponse();

        Object content = null;
        //
        if (body instanceof ResponseBody) {
            ResponseBody responseResult = ((ResponseBody) body);
            Object contentObject = responseResult.getContent();
            try {
                content = this.getContentAnEncode(contentObject, responseSecretItem.getKey(), responseSecretItem.getIv(), response);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                responseResult.setMessage(e.getMessage());
            }
            responseResult.setContent(content);
            return body;
        }

        // 加密
        try {
            content = this.getContentAnEncode(body, responseSecretItem.getKey(), responseSecretItem.getIv(), response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return content;
    }

    private String getContentAnEncode(Object contentObject, String key, String iv, ServerHttpResponse response) throws JsonProcessingException {
        String content = null;
        if (contentObject != null) {
            if (contentObject instanceof String) {
                content = (String) contentObject;
            } else {
                content = objectMapper.writeValueAsString(contentObject);
            }
            if (!Strings.isNullOrEmpty(content)) {
                content = AesCbcUtil.encode(content, key, iv);
            }
        }
        // 设置响应头，让前端解密
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.set("X-App-Response-Secret", "true");
        return content;
    }

}
