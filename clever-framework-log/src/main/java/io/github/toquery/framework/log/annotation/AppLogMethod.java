package io.github.toquery.framework.log.annotation;

import io.github.toquery.framework.log.constant.AppLogType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法aop拦截，如果以$开头，则默认获取实体相应字段
 *
 * @author toquery
 * @version 1
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppLogMethod {


    Class value();

//    @AliasFor("value")
//    Class entity();

    AppLogType logType();

    String modelName() default "system";

    String bizName() default "system";
}
