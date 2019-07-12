package io.github.toquery.framework.webmvc.resolver;

import io.github.toquery.framework.webmvc.annotation.UpperCase;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 解决 MethodArgument 转换为大写
 *
 * @author toquery
 * @version 1
 */
public class MethodArgumentUpperCaseResolver implements HandlerMethodArgumentResolver {

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(UpperCase.class) != null;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        UpperCase attr = parameter.getParameterAnnotation(UpperCase.class);
        return webRequest.getParameter(attr.value()).toUpperCase();
    }
}
