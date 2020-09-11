package io.github.toquery.framework.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class JacksonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final TypeReference<HashMap<String, Object>> TYPE_REFERENCE_HASHMAP = new TypeReference<HashMap<String, Object>>() {
    };


    public static final String DEFAULT_EMPTY_JSON_OBJECT = "{}";

    public static final String DEFAULT_EMPTY_JSON_ARRAY = "[]";

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = createDefaultObjectMapper();

    /**
     * 创建默认配置的ObjectMapper
     */
    public static ObjectMapper createDefaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //通过以下三项配置来开启仅以属性字段来序列化和反序列化对象(忽略get方法)
        //objectMapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
        //objectMapper.disable(MapperFeature.AUTO_DETECT_IS_GETTERS);
        //objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        //将被序列化对象的类名作为一个字段(字段名@class)输出到序列化后的JSON字符串中
        //objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        // 建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用
        //defaultObjectMapper.setSerializationInclusion(Include.NON_DEFAULT);
        //去掉默认的时间戳格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //objectMapper.getDeserializationConfig().getDateFormat().setTimeZone(zone);
        objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //单引号处理,允许单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //允许wrap/unwrap
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);

        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public static ObjectMapper getDefaultObjectMapper() {
        return DEFAULT_OBJECT_MAPPER;
    }

    /**
     * object 转为 map
     */
    public static Map<String, Object> object2HashMap(Object object) {
        return objectMapper.convertValue(object, TYPE_REFERENCE_HASHMAP);
    }


    /**
     * string 转为 map
     */
    public static Map<String, Object> string2HashMap(String jsonString) throws IOException {
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
