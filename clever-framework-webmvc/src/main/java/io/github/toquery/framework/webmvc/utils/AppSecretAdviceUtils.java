package io.github.toquery.framework.webmvc.utils;

import cn.hutool.json.JSONUtil;
import io.github.toquery.framework.common.util.AesCbcUtil;
import io.github.toquery.framework.webmvc.properties.AppWebMvcSecretProperties;
import io.github.toquery.framework.webmvc.secret.annotation.RequestIgnoreSecret;
import io.github.toquery.framework.webmvc.secret.annotation.RequestSecret;
import io.github.toquery.framework.webmvc.secret.annotation.ResponseIgnoreSecret;
import io.github.toquery.framework.webmvc.secret.annotation.ResponseSecret;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;

/**
 *
 */
@Slf4j
public class AppSecretAdviceUtils {

    /**
     * 判断是否需要加密
     */
    public static boolean requestNeedEncrypt(AppWebMvcSecretProperties.AppWebMvcSecretItem appWebMvcSecretItem, Class<?> currentClass, MethodParameter methodParameter) {

        //异常处理类注解
        Annotation controllerAdvice = currentClass.getAnnotation(ControllerAdvice.class);
        Annotation restControllerAdvice = currentClass.getAnnotation(RestControllerAdvice.class);

        if(controllerAdvice != null || restControllerAdvice != null){
            return false;
        }

        // 只处理 restcontroller 或 responsebody 的方法、类
        RestController restControllerAnnotation = currentClass.getAnnotation(RestController.class);
        ResponseBody responseBodyAnnotation = currentClass.getAnnotation(ResponseBody.class);
        //方法注解
        ResponseBody responseBodyMethodAnnotation = methodParameter.getMethodAnnotation(ResponseBody.class);

        if (restControllerAnnotation == null && responseBodyAnnotation == null && responseBodyMethodAnnotation == null) {
            log.debug("App 请求加密 只处理 RestController 或 ResponseBody 的方法、类");
            return false;
        }

        //类注解
        Annotation secretClassAnnotation = currentClass.getAnnotation(RequestSecret.class);
        //方法注解
        RequestSecret secretMethodAnnotation = methodParameter.getMethodAnnotation(RequestSecret.class);

        //类注解
        Annotation ignoreSecretClassAnnotation = currentClass.getAnnotation(RequestIgnoreSecret.class);
        //方法注解
        RequestIgnoreSecret ignoreSecretMethodAnnotation = methodParameter.getMethodAnnotation(RequestIgnoreSecret.class);



        if (appWebMvcSecretItem.isEnabled()) { // 开启了加密
            //如果类与方法均不存在注解，排除,
            return ignoreSecretClassAnnotation == null && ignoreSecretMethodAnnotation == null;
        } else { // 关闭了加密
            return secretClassAnnotation != null || secretMethodAnnotation != null;
        }
    }

    /**
     * 判断是否需要加密
     */
    public static boolean responseNeedEncrypt(AppWebMvcSecretProperties.AppWebMvcSecretItem appWebMvcSecretItem, Class<?> currentClass, MethodParameter methodParameter) {
        //异常处理类注解
        Annotation controllerAdvice = currentClass.getAnnotation(ControllerAdvice.class);
        Annotation restControllerAdvice = currentClass.getAnnotation(RestControllerAdvice.class);

        if(controllerAdvice != null || restControllerAdvice != null){
            return false;
        }

        // 只处理 RestController 或 ResponseBody 的方法、类
        RestController restControllerAnnotation = currentClass.getAnnotation(RestController.class);
        ResponseBody responseBodyAnnotation = currentClass.getAnnotation(ResponseBody.class);
        //方法注解
        ResponseBody responseBodyMethodAnnotation = methodParameter.getMethodAnnotation(ResponseBody.class);

        if (restControllerAnnotation == null && responseBodyAnnotation == null && responseBodyMethodAnnotation == null) {
            log.debug("App 响应加密 只处理 RestController 或 ResponseBody 的方法、类");
            return false;
        }

        //类注解
        Annotation secretClassAnnotation = currentClass.getAnnotation(ResponseSecret.class);
        //方法注解¬
        ResponseSecret secretMethodAnnotation = methodParameter.getMethodAnnotation(ResponseSecret.class);

        //类注解
        Annotation ignoreSecretClassAnnotation = currentClass.getAnnotation(ResponseIgnoreSecret.class);
        //方法注解
        ResponseIgnoreSecret ignoreSecretMethodAnnotation = methodParameter.getMethodAnnotation(ResponseIgnoreSecret.class);

        if (appWebMvcSecretItem.isEnabled()) { // 开启了加密
            //如果类与方法均不存在注解，排除,
            return ignoreSecretClassAnnotation == null && ignoreSecretMethodAnnotation == null;
        } else { // 关闭了加密
            return secretClassAnnotation != null || secretMethodAnnotation != null;
        }
    }

    /**
     * 解密消息体,3des解析（cbc模式）
     *
     * @param inputMessage 消息体
     * @return 明文
     */
    public static String decryptBody(HttpInputMessage inputMessage, String secretKey, String iv) throws IOException {
        InputStream encryptStream = inputMessage.getBody();
        String encryptBody = StreamUtils.copyToString(encryptStream, Charset.defaultCharset());
        if (JSONUtil.isJson(encryptBody)) {
            // 是json格式，并且非强制加密
            return encryptBody;
        }
        return AesCbcUtil.decode(encryptBody, secretKey, iv);

    }
}
