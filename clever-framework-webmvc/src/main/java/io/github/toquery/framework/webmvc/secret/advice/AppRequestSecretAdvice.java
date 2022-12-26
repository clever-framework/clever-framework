package io.github.toquery.framework.webmvc.secret.advice;

import io.github.toquery.framework.webmvc.properties.AppWebMvcProperties;
import io.github.toquery.framework.webmvc.properties.AppWebMvcSecretProperties;
import io.github.toquery.framework.webmvc.secret.AppSecretHttpMessage;
import io.github.toquery.framework.webmvc.secret.AppWebMvcSecretException;
import io.github.toquery.framework.webmvc.utils.AppSecretAdviceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import jakarta.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 *
 */
@Slf4j
@ControllerAdvice
public class AppRequestSecretAdvice extends RequestBodyAdviceAdapter {

    @Resource
    private AppWebMvcProperties appWebMvcProperties;

    public AppRequestSecretAdvice() {
        log.debug("AppRequestSecretAdvice init");
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.debug("AppRequestSecretAdvice supports methodParameter:{}, targetType:{}, converterType:{}", methodParameter, targetType, converterType);
        Class<?> currentClass = methodParameter.getContainingClass();
        AppWebMvcSecretProperties.AppWebMvcSecretItem request = appWebMvcProperties.getSecret().getRequest();

        log.debug("App 请求加密 正在判断 {} : {} : {}", currentClass.getName(), methodParameter.getMethod().getName(), methodParameter.getMethod().getReturnType().getName());

        return AppSecretAdviceUtils.requestNeedEncrypt(request, currentClass, methodParameter);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        log.debug("inputMessage.getClass {}", inputMessage.getClass().getName());

        log.debug("AppRequestSecretAdvice beforeBodyRead inputMessage:{}, parameter:{}, targetType:{}, converterType:{}", inputMessage, parameter, targetType, converterType);

        Class<?> currentClass = parameter.getContainingClass();

        AppWebMvcSecretProperties.AppWebMvcSecretItem request = appWebMvcProperties.getSecret().getRequest();

        log.debug("App 请求加密 正在处理 {} : {} : {}", currentClass.getName(), parameter.getMethod().getName(), parameter.getMethod().getReturnType().getName());

        String httpBody = AppSecretAdviceUtils.decryptBody(inputMessage, request.getKey(), request.getIv());
        if (httpBody == null) {
            throw new AppWebMvcSecretException("request body decrypt error");
        }
        return new AppSecretHttpMessage(new ByteArrayInputStream(httpBody.getBytes()), inputMessage.getHeaders());
    }
}
