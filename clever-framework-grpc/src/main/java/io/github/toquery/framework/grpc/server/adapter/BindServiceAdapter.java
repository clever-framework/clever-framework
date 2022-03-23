package io.github.toquery.framework.grpc.server.adapter;

import io.github.toquery.framework.common.util.ReflectUtils;
import io.github.toquery.framework.grpc.core.exception.GRpcMethodNoMatchException;
import io.github.toquery.framework.grpc.core.factory.MarshallerFactory;
import io.github.toquery.framework.grpc.core.model.MethodCallProperty;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.grpc.*;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @since 2019/1/16
 */
@Data
public class BindServiceAdapter implements BindableService {
    private final String scheme;
    private final List<MethodCallProperty> methodCallList;
    private final MarshallerFactory marshallerFactory;

    public BindServiceAdapter(String scheme, List<MethodCallProperty> methodCallList, MarshallerFactory marshallerFactory) {
        this.scheme = scheme;
        this.methodCallList = methodCallList;
        this.marshallerFactory = marshallerFactory;
    }


    private MethodDescriptor<Object, Object> createMethodDescriptor(MethodCallProperty methodCallProperty) {
        return MethodDescriptor.newBuilder(
                marshallerFactory.parseRequestMarshaller(methodCallProperty),
                marshallerFactory.emptyMarshaller()
        ).setType(methodCallProperty.getMethodType())
                .setFullMethodName(MethodDescriptor.generateFullMethodName(methodCallProperty.getScheme(), methodCallProperty.getMethodName().startsWith("/") ? methodCallProperty.getMethodName().substring(1) : methodCallProperty.getMethodName()))
                .build();
    }

    /**
     * 检查方法是否包含两个参数，一个为业务实体，另外一个为StreamObserver
     *
     * @param method     方法
     * @param methodType methodType
     */
    private void checkTwoParamHasStreamObServer(Method method, MethodDescriptor.MethodType methodType) {
        //判断当前方法是否仅包含两个参数，一个为请求实体，一个为StreamObserver。如果不是，抛出异常。
        Type[] types = method.getGenericParameterTypes();
        if (types == null || types.length != 2) {
            throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodType.name(),
                    "You should use two parameters in the method, one is [Business Request Model] and the other is [StreamObserver].And the order must be consistent.Please check it.");
        }
        //检查第二个参数是否为StreamObserver
        Type type = ReflectUtils.safeElement(types, 1);
        if (type instanceof ParameterizedType) {
            if (!((ParameterizedType) type).getRawType().getTypeName().equals(StreamObserver.class.getName())) {
                throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodType.name(),
                        "You should use two parameters in the method, one is [Business Request Model] and the other is [StreamObserver].And the order must be consistent.Please check it.");
            }
        } else {
            if (!StreamObserver.class.getName().equals(type.getTypeName())) {
                throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodType.name(),
                        "You should use two parameters in the method, one is [Business Request Model] and the other is [StreamObserver].And the order must be consistent.Please check it.");
            }
        }

    }


    private void invokeMethodWithParamSize(Object target, Method method, int paramSize, Object request, StreamObserver<Object> responseObserver, MethodDescriptor.MethodType methodType) {
        try {
            if (paramSize == 2) {
                checkTwoParamHasStreamObServer(method, methodType);
                method.invoke(target, request, responseObserver);
            } else if (paramSize == 1) {
                //判断这个方法是StreamObserver还是业务实体
                if (ReflectUtils.checkMethodHasParamClass(method, StreamObserver.class)) {
                    //传递StreamObserver
                    method.invoke(target, responseObserver);
                } else {
                    //业务实体，将业务传递，返回空
                    method.invoke(target, request);
                    responseObserver.onNext(null);
                    responseObserver.onCompleted();
                }
            } else if (paramSize == 0) {
                method.invoke(target);
                responseObserver.onNext(null);
                responseObserver.onCompleted();
            }
        } catch (Exception exception) {
            if(exception instanceof InvocationTargetException){
                InvocationTargetException invocationTargetException = (InvocationTargetException)exception;
                responseObserver.onNext(ResponseBodyWrap.builder().fail().message(invocationTargetException.getMessage()).build());
                responseObserver.onCompleted();
                return;
            }
            responseObserver.onNext(ResponseBodyWrap.builder().fail().message(exception.getMessage()).build());
            responseObserver.onCompleted();
        }
    }

    @SuppressWarnings("unchecked")
    private ServerCallHandler<Object, Object> parseCallHandlerFromType(MethodCallProperty methodCallProperty) {
        Method method = methodCallProperty.getMethod();
        Object target = methodCallProperty.getProxyTarget();
        int paramSize = method.getGenericParameterTypes().length;

        switch (methodCallProperty.getMethodType()) {
            case UNARY:
                return ServerCalls.asyncUnaryCall((request, responseObserver) -> {
                    invokeMethodWithParamSize(target, method, paramSize, request, responseObserver, MethodDescriptor.MethodType.UNARY);

                });
            case SERVER_STREAMING:
                return ServerCalls.asyncServerStreamingCall((request, responseObserver) -> {
                    invokeMethodWithParamSize(target, method, paramSize, request, responseObserver, MethodDescriptor.MethodType.SERVER_STREAMING);
                });
            case CLIENT_STREAMING:
                return ServerCalls.asyncClientStreamingCall(responseObserver -> {
                    try {
                        return (StreamObserver<Object>) method.invoke(target, responseObserver);
                    } catch (Exception exception) {
                        ResponseBodyWrap messageBody = ResponseBodyWrap.builder().fail().message(exception.getMessage()).build();
                        return new StreamObserver<Object>() {
                            @Override
                            public void onNext(Object value) {
                                responseObserver.onNext(messageBody);
                            }

                            @Override
                            public void onError(Throwable t) {
                            }

                            @Override
                            public void onCompleted() {
                                responseObserver.onCompleted();
                            }
                        };
                    }
                });
            case BIDI_STREAMING:
                return ServerCalls.asyncBidiStreamingCall(responseObserver -> {
                    try {
                        return (StreamObserver<Object>) method.invoke(target, responseObserver);
                    } catch (Exception exception) {
                        ResponseBodyWrap messageBody = ResponseBodyWrap.builder().fail().message(exception.getMessage()).build();
                        return new StreamObserver<Object>() {
                            @Override
                            public void onNext(Object value) {
                                responseObserver.onNext(messageBody);
                            }

                            @Override
                            public void onError(Throwable t) {
                            }

                            @Override
                            public void onCompleted() {
                                responseObserver.onCompleted();
                            }
                        };
                    }
                });
            default:
                return ServerCalls.asyncUnaryCall((request, responseObserver) -> {
                    responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND.withDescription("GRpc method type not match.Please check your class.")));
                });
        }
    }

    /**
     * 创建监听服务
     *
     * @return 监听服务定义
     */
    @Override
    public ServerServiceDefinition bindService() {
        ServerServiceDefinition.Builder builder = ServerServiceDefinition.builder(scheme);
        for (MethodCallProperty methodCallProperty : methodCallList) {
            //根据方法类型，获取不同的handler监听
            builder.addMethod(createMethodDescriptor(methodCallProperty), parseCallHandlerFromType(methodCallProperty));
        }
        return builder.build();
    }
}
