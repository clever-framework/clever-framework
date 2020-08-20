package io.github.toquery.framework.web.dict;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典项标识
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE , ElementType.FIELD})
@Documented
public @interface AppDict {

    /**
     * 字典项父节点编码
     */
    String value() default "";

}
