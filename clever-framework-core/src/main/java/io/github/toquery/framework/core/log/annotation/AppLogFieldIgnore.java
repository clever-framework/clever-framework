package io.github.toquery.framework.core.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略记录实体的某个字段，如果全局中配置，则会相加
 *
 * @author toquery
 * @version 1
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppLogFieldIgnore {

}
