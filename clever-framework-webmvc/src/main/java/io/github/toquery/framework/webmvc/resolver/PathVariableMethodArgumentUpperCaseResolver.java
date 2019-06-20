package io.github.toquery.framework.webmvc.resolver;

import io.github.toquery.framework.webmvc.annotation.PathVariableUpperCase;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * 解决 PathVariable 路径转换为大写
 *
 * @author toquery
 * @version 1
 */
public class PathVariableMethodArgumentUpperCaseResolver extends PathVariableMethodArgumentResolver {
    public PathVariableMethodArgumentUpperCaseResolver() {
        super();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (!parameter.hasParameterAnnotation(PathVariableUpperCase.class)) {
            return false;
        }
        if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
            PathVariableUpperCase pathVariable = parameter.getParameterAnnotation(PathVariableUpperCase.class);
            return (pathVariable != null && StringUtils.hasText(pathVariable.value()));
        }
        return true;
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        PathVariableUpperCase ann = parameter.getParameterAnnotation(PathVariableUpperCase.class);
        Assert.state(ann != null, "No PathVariable annotation");
        return new PathVariableNamedValueInfo(ann);
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        Object object = super.resolveName(name, parameter, request);
        if (object != null && object.getClass() == String.class)
            object = ((String) object).toUpperCase();
        return object;
    }

    @Override
    public void contributeMethodArgument(MethodParameter parameter, Object value, UriComponentsBuilder builder, Map<String, Object> uriVariables, ConversionService conversionService) {

        if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
            return;
        }

        PathVariableUpperCase ann = parameter.getParameterAnnotation(PathVariableUpperCase.class);
        String name = (ann != null && StringUtils.hasLength(ann.value()) ? ann.value() : parameter.getParameterName());
        String formatted = formatUriValue(conversionService, new TypeDescriptor(parameter.nestedIfOptional()), value);
        uriVariables.put(name, formatted);
    }

    private static class PathVariableNamedValueInfo extends NamedValueInfo {

        public PathVariableNamedValueInfo(PathVariableUpperCase annotation) {
            super(annotation.name(), annotation.required(), ValueConstants.DEFAULT_NONE);
        }
    }

}
