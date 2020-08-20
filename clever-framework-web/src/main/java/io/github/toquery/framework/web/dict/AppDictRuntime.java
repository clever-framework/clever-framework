package io.github.toquery.framework.web.dict;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.toquery.framework.web.dict.serializer.AppDictDeserializer;
import io.github.toquery.framework.web.dict.serializer.AppDictSerializer;

/**
 * 应用运行字典项
 */
@JsonSerialize(using = AppDictSerializer.class)
@JsonDeserialize(using = AppDictDeserializer.class)
public interface AppDictRuntime {
    /**
     * 字典编码
     */
    String name() ;

    /**
     * 字典项说明信息
     */
    String getRemark() ;
}
