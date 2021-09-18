package io.github.toquery.framework.grpc.client.factory;

import io.github.toquery.framework.grpc.client.annotation.GRpcClient;
import io.github.toquery.framework.grpc.client.exception.GRpcChannelCreateException;
import io.github.toquery.framework.grpc.client.model.ChannelProperty;
import io.github.toquery.framework.grpc.client.proxy.ManageChannelProxy;
import io.github.toquery.framework.grpc.core.annotation.GRpcMethod;
import io.github.toquery.framework.grpc.core.factory.MarshallerFactory;
import io.github.toquery.framework.grpc.core.model.MethodCallProperty;
import io.grpc.ProxyDetector;
import lombok.Data;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 2019/1/15
 */
@Data
public class ClientFactory {
    private final MarshallerFactory marshallerFactory;
    private final ProxyDetector proxyDetector;
    /**
     * 服务名-连接配置 字典，factory创建时设置
     */
    private Map<String, ChannelProperty> serverChannelMap = new HashMap<>();

    public ClientFactory(MarshallerFactory marshallerFactory,  ProxyDetector proxyDetector) {
        this.marshallerFactory = marshallerFactory;
        this.proxyDetector = proxyDetector;
    }

    public Object createClientProxy(Class<?> target) {
        GRpcClient gRpcClient = target.getAnnotation(GRpcClient.class);
        ChannelProperty channelProperty = serverChannelMap.get(gRpcClient.value());
        if (channelProperty == null) {
            throw new GRpcChannelCreateException("GRpcService scheme:{" + gRpcClient.value() + "} was not found in properties.Please check your configuration.");
        }
        ManageChannelProxy manageChannelProxy = new ManageChannelProxy(channelProperty, marshallerFactory, proxyDetector);
        //获取该类下所有包含GrpcMethod的注解，创建call定义
        Map<Method, GRpcMethod> annotatedMethods = MethodIntrospector.selectMethods(target,
                (MethodIntrospector.MetadataLookup<GRpcMethod>) method -> AnnotatedElementUtils.findMergedAnnotation(method, GRpcMethod.class));
        annotatedMethods.forEach((k, v) -> {
            String annotationMethodName = v.value();
            MethodCallProperty methodCallProperty = new MethodCallProperty();
            methodCallProperty.setMethod(k);
            methodCallProperty.setMethodName(StringUtils.hasText(annotationMethodName) ? k.getName() : annotationMethodName);
            methodCallProperty.setMethodType(v.type());
            methodCallProperty.setScheme(gRpcClient.scheme());
            manageChannelProxy.addCall(methodCallProperty);
        });
        return Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target}, manageChannelProxy);
    }
}
