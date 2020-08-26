package io.github.toquery.framework.core.log.annotation;


import io.github.toquery.framework.core.log.AppLogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法aop拦截，如果以$开头，则默认获取实体相应字段，如果是接受参数非实体，则获取接受参数的值
 *
 * @author toquery
 * @version 1
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppLogMethod {

    Class value();

    AppLogType logType();

    String modelName() default "system";

    String bizName() default "system";
}
