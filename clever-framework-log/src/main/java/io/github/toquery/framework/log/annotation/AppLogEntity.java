package io.github.toquery.framework.log.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author toquery
 * @version 1
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppLogEntity {

    @AliasFor("modelName")
    String value() default "";

    @AliasFor("value")
    String modelName() default "system";

    @AliasFor("value")
    String bizName() default "system";
}
