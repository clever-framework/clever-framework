package io.github.toquery.framework.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JacksonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final TypeReference<HashMap<String, Object>> TYPE_REFERENCE_HASHMAP = new TypeReference<HashMap<String, Object>>() {
    };

    /**
     * object 转为 map
     */
    public static Map<String, Object> object2HashMap(Object object) {
        return objectMapper.convertValue(object, TYPE_REFERENCE_HASHMAP);
    }

    /**
     * string 转为 map
     */
    public static Map<String, Object> string2Map(String jsonString) throws IOException {
        return objectMapper.readValue(jsonString, TYPE_REFERENCE_HASHMAP);
    }


    /**
     * map 转为 string
     */
    public static String map2String(Map<String, Object> attribute) throws JsonProcessingException {
        return objectMapper.writeValueAsString(attribute);
    }

    public static String object2String(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
