package io.github.toquery.framework.grpc.server.processor;

import io.github.toquery.framework.grpc.core.annotation.GRpcMethod;
import io.github.toquery.framework.grpc.core.factory.MarshallerFactory;
import io.github.toquery.framework.grpc.core.model.MethodCallProperty;
import io.github.toquery.framework.grpc.server.adapter.BindServiceAdapter;
import io.github.toquery.framework.grpc.server.annotation.GRpcServer;
import io.github.toquery.framework.grpc.server.exception.GRpcServerCreateException;
import lombok.Data;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @since 2019/1/16
 */
@Data
public class GRpcServiceProcessor implements BeanPostProcessor {
    private List<BindServiceAdapter> bindServiceAdapterList = new ArrayList<>();
    private final MarshallerFactory marshallerFactory;

    public GRpcServiceProcessor(MarshallerFactory marshallerFactory) {
        this.marshallerFactory = marshallerFactory;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        GRpcServer grpcServer = bean.getClass().getAnnotation(GRpcServer.class);
        if (grpcServer == null) {
            return bean;
        }
        String scheme = grpcServer.value();
        //检验scheme是否存在
        if (bindServiceAdapterList.stream().anyMatch(item -> item.getScheme().equals(scheme))) {
            throw new GRpcServerCreateException("The scheme " + "[" + scheme + "] is already exist.Please check your configuration.");
        }
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        Map<Method, GRpcMethod> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                (MethodIntrospector.MetadataLookup<GRpcMethod>) method -> AnnotatedElementUtils.findMergedAnnotation(method, GRpcMethod.class));
        if (annotatedMethods.size() == 0) {
            return bean;
        }
        List<MethodCallProperty> methodCallPropertyList = new ArrayList<>();
        annotatedMethods.forEach((method, v) -> {
            MethodCallProperty methodCallProperty = new MethodCallProperty();
            methodCallProperty.setScheme(scheme);
            methodCallProperty.setMethod(method);
            methodCallProperty.setProxyTarget(bean);
            methodCallProperty.setMethodName(StringUtils.hasText(v.value()) ? method.getName() : v.value());
            methodCallProperty.setMethodType(v.type());
            methodCallPropertyList.add(methodCallProperty);
        });
        BindServiceAdapter bindServiceAdapter = new BindServiceAdapter(scheme, methodCallPropertyList, marshallerFactory);
        bindServiceAdapterList.add(bindServiceAdapter);
        return bean;
    }
}
