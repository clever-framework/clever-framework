package io.github.toquery.framework.grpc.core.model;

import io.grpc.MethodDescriptor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @since 2019/1/14
 */
@Data
public class MethodCallProperty {
    private String methodName;
    private MethodDescriptor.MethodType methodType;
    private String scheme;
    private Method method;
    private Object proxyTarget;
}
