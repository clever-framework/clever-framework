package io.github.toquery.framework.datasource;

import io.github.toquery.framework.datasource.config.DataSourceContextHolder;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceSwitch {

    String value() default DataSourceContextHolder.DEFAULT_DATA_SOURCE;
}
