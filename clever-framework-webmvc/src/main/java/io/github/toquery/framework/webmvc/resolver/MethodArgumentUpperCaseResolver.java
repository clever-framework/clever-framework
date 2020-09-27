package io.github.toquery.framework.webmvc.resolver;

import com.google.common.base.Strings;
import io.github.toquery.framework.webmvc.annotation.UpperCase;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MethodArgumentUpperCaseResolver implements HandlerMethodArgumentResolver {

    public MethodArgumentUpperCaseResolver() {
        log.debug("初始化自定义参数大写转换 {}", this.getClass().getName());
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(UpperCase.class) != null &&
                (String.class.isAssignableFrom(parameter.getParameterType()) || Enum.class.isAssignableFrom(parameter.getParameterType()) );
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String parameterName = parameter.getParameterName();
        if (Strings.isNullOrEmpty(parameterName)) {
            return parameterName;
        }
        String parameterValue = webRequest.getParameter(parameterName);
        if (Strings.isNullOrEmpty(parameterValue)) {
            return parameterValue;
        }
        return parameterValue.toUpperCase();
    }
}
