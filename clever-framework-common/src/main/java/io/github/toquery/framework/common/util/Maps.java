package io.github.toquery.framework.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Maps {

    public static Map<String, Object> entity2Map(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {});
    }
}
