package io.github.toquery.framework.web.dict.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.toquery.framework.web.dict.AppDictRuntime;

import java.io.IOException;

/**
 * 将枚举对象序列化为json 格式为 {"code": "MALE", "name":"男"}
 */
public class AppDictSerializer extends StdSerializer<AppDictRuntime> {

    public AppDictSerializer() {
        super(AppDictRuntime.class);
    }

    public AppDictSerializer(Class<AppDictRuntime> appRuntimeDict) {
        super(appRuntimeDict);
    }


    @Override
    public void serialize(AppDictRuntime appRuntimeDict, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeStartObject();
        generator.writeFieldName("code");
        generator.writeString(appRuntimeDict.name());
        generator.writeFieldName("name");
        generator.writeString(appRuntimeDict.getRemark());
        generator.writeEndObject();
    }
}
