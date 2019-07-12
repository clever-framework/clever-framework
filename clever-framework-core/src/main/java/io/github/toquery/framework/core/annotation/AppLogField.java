package io.github.toquery.framework.core.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识转换需要记录日志的字段名称
 *
 * @author toquery
 * @version 1
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppLogField {

    /**
     * 实体的标识，新增 删除的时候会以标识 为 true 的字段展示
     * @return false 不是唯一标志
     */
    boolean uniqueFlag() default false;

    /**
     * 要显示的名称
     * @return 显示的名称
     */
    @AliasFor("name")
    String value() default "";

    /**
     * 要显示的名称
     * @return 显示的名称
     */
    @AliasFor("value")
    String name() default "";
}
