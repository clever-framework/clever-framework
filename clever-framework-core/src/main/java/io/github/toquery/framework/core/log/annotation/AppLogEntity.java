package io.github.toquery.framework.core.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 记录业务日志的实体注解，用于实体类
 *
 * @author toquery
 * @version 1
 */
@Deprecated
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppLogEntity {

    // @AliasFor("modelName")
    // String value() default "";

    // @AliasFor("value")
    String modelName() default "system";

    // @AliasFor("value")
    String bizName() default "system";
}
