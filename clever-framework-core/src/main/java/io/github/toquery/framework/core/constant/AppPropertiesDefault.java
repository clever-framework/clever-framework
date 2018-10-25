package io.github.toquery.framework.core.constant;

/**
 * spring 的 app配置文件的默认值
 *
 * @author toquery
 * @version 1
 */
public class AppPropertiesDefault {

    /**
     * 默认日期格式
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 默认时间格式
     */
    public static final String TIME_PATTERN = "HH:mm:ss";
    /**
     * 默认日期时间格式
     */
    public static final String DATE_TIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;


    /**
     * 默认是否删除属性的数据库对应字段名称
     */
    public static final String JPA_COLUMN_SOFT_DEL = "is_del";
}
