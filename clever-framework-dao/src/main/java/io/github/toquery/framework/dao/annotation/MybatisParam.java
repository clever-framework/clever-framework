package io.github.toquery.framework.dao.annotation;

import org.apache.ibatis.annotations.Param;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author toquery
 * @version 1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface MybatisParam {

    @AliasFor(attribute = "value", annotation = Param.class)
    String value();
}
