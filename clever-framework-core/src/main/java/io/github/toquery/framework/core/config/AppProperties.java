package io.github.toquery.framework.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author toquery
 * @version 1
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 默认日期时间格式
     */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";


    public static final String JPA_COLUMN_SOFT_DEL = "is_del";
}
