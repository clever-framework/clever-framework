package io.github.toquery.framework.web.dict.serializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * 将json反序列化为枚举  {"code": "MALE", "name":"男"}
 */
public class AppDictDeserializer extends JsonDeserializer<Enum<?>> {

    @Override
    public Enum<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String currentName = jsonParser.currentName();
        Object currentValue = jsonParser.getCurrentValue();

        Class findPropertyType = null;
        if(currentValue instanceof Collection) {

            JsonStreamContext parsingContext = jsonParser.getParsingContext();

            JsonStreamContext parent = parsingContext.getParent();
            Object currentValue3 = parent.getCurrentValue();
            String currentName3 = parent.getCurrentName();
            try {
                Field listField = currentValue3.getClass().getDeclaredField(currentName3);
                ParameterizedType listGenericType = (ParameterizedType) listField.getGenericType();
                Type listActualTypeArguments = listGenericType.getActualTypeArguments()[0];
                findPropertyType = (Class) listActualTypeArguments;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            findPropertyType = BeanUtils.findPropertyType(currentName, currentValue.getClass());
        }
        if(findPropertyType == null) {
            throw new IOException("数据格式异常");
        }

        JsonFormat annotation = (JsonFormat) findPropertyType.getAnnotation(JsonFormat.class);
        Enum<?> valueOf = null;
        if (annotation == null){
            valueOf =  Enum.valueOf(findPropertyType, node.get("code").asText());
        } else if(annotation.shape() == JsonFormat.Shape.STRING) {
            valueOf =  Enum.valueOf(findPropertyType, node.asText());
        }
        return valueOf;

    }
}
