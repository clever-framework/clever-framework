package io.github.toquery.framework.grpc.client.proxy;

import io.github.toquery.framework.common.util.ReflectUtils;
import io.github.toquery.framework.grpc.client.model.ChannelProperty;
import io.github.toquery.framework.grpc.core.annotation.GRpcMethod;
import io.github.toquery.framework.grpc.core.factory.MarshallerFactory;
import io.github.toquery.framework.grpc.core.model.MethodCallProperty;
import com.google.common.util.concurrent.ListenableFuture;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.grpc.*;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 2019/1/14
 */
public class ManageChannelProxy implements InvocationHandler {
    private final MarshallerFactory marshallerFactory;
    private final ManagedChannel channel;
    private final Object invoker = new Object();
    private final Map<String, MethodCallProperty> callDefinitions = new HashMap<>();

    public ManageChannelProxy(ChannelProperty channelProperty, MarshallerFactory marshallerFactory, ProxyDetector proxyDetector) {
        this.marshallerFactory = marshallerFactory;
        this.channel = ManagedChannelBuilder.forAddress(channelProperty.getHost(), channelProperty.getPort())
                .usePlaintext()
                .proxyDetector(proxyDetector)
                .build();
    }

    public void addCall(MethodCallProperty methodCallProperty) {
        callDefinitions.put(methodCallProperty.getMethodName(), methodCallProperty);
    }

    private ClientCall<Object, Object> buildCall(MethodCallProperty methodCallProperty) {
        MethodDescriptor.Builder<Object, Object> builder = MethodDescriptor.newBuilder(
                marshallerFactory.emptyMarshaller(),
                marshallerFactory.parseReturnMarshaller(methodCallProperty)
        ).setType(methodCallProperty.getMethodType())
                .setFullMethodName(MethodDescriptor.generateFullMethodName(methodCallProperty.getScheme(), methodCallProperty.getMethodName().startsWith("/") ? methodCallProperty.getMethodName().substring(1) : methodCallProperty.getMethodName()));
        return channel.newCall(builder.build(), CallOptions.DEFAULT);
    }


    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        String methodName = method.getName();
        String className = method.getDeclaringClass().getName();
        if ("toString".equals(methodName) && args.length == 0) {
            return className + "@" + invoker.hashCode();
        } else if ("hashCode".equals(methodName) && args.length == 0) {
            return invoker.hashCode();
        } else if ("equals".equals(methodName) && args.length == 1) {
            Object another = ReflectUtils.safeElement(args, 0);
            return proxy == another;
        }
        String annotationMethodName = method.getAnnotation(GRpcMethod.class).value();
        MethodCallProperty methodCallProperty = callDefinitions.get(StringUtils.hasText(annotationMethodName) ? methodName : annotationMethodName);
        ClientCall<Object, Object> clientCall = buildCall(methodCallProperty);
        switch (methodCallProperty.getMethodType()) {
            case UNARY:
                if (method.getReturnType() == ListenableFuture.class) { //等于ClientCalls.futureUnaryCall()
                    return ClientCalls.futureUnaryCall(clientCall, ReflectUtils.safeElement(args, 0));
                } else if (method.getReturnType().getName().equals("void")) { //等于ClientCalls
                    try {
                        if (ReflectUtils.checkMethodHasParamClass(method, StreamObserver.class)) {
                            ClientCalls.asyncUnaryCall(clientCall, ReflectUtils.safeElement(args, 0), (StreamObserver<Object>) ReflectUtils.safeElement(args, 1));
                            return null;
                        } else {
                            ClientCalls.blockingUnaryCall(clientCall, ReflectUtils.safeElement(args, 0));
                            return null;
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return null;
                    }
                }
                try {
                    return ClientCalls.blockingUnaryCall(clientCall, ReflectUtils.safeElement(args, 0));
                } catch (Exception exception) {
                    exception.printStackTrace();
                    return ResponseBodyWrap.builder().fail().message(exception.getMessage()).build();
                }

            case BIDI_STREAMING://双向流，相当于asyncBidiStreamingCall
                //获取返回类型的泛型
                return ClientCalls.asyncBidiStreamingCall(clientCall, (StreamObserver<Object>) ReflectUtils.safeElement(args, 0));
            case CLIENT_STREAMING: //客户端流。等于ClientCalls.asyncClientStreamingCall()
                return ClientCalls.asyncClientStreamingCall(clientCall, (StreamObserver<Object>) ReflectUtils.safeElement(args, 0));
            case SERVER_STREAMING://等于ClientCalls.blockingServerStreamingCall
                return ClientCalls.blockingServerStreamingCall(clientCall, ReflectUtils.safeElement(args, 0));
        }
        return null;
    }

}
