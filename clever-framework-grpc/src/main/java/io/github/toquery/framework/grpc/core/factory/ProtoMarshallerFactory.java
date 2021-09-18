package io.github.toquery.framework.grpc.core.factory;

import io.github.toquery.framework.grpc.core.marshaller.ProtoMarshaller;
import io.grpc.MethodDescriptor;

import java.lang.reflect.Type;

/**
 * @since 2019/1/22
 */
public class ProtoMarshallerFactory extends AbstractMarshallerFactory {

    @Override
    protected MethodDescriptor.Marshaller<Object> generateMarshaller(Type type) {
        return new ProtoMarshaller(type);
    }
}
