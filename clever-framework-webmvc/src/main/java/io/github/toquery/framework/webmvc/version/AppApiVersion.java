package io.github.toquery.framework.webmvc.version;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author toquery
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppApiVersion {
    /**
     * @return 版本号
     */
    int value() default 0;
}
