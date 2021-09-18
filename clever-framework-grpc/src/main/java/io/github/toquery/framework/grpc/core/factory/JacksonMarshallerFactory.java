package io.github.toquery.framework.grpc.core.factory;

import io.github.toquery.framework.grpc.core.marshaller.JacksonMarshaller;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.MethodDescriptor;

import java.lang.reflect.Type;

/**
 * @since 2019/1/22
 */
public class JacksonMarshallerFactory extends AbstractMarshallerFactory {
    private final ObjectMapper objectMapper;


    public JacksonMarshallerFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    protected MethodDescriptor.Marshaller<Object> generateMarshaller(Type type) {
        return new JacksonMarshaller(type, objectMapper);
    }

}
