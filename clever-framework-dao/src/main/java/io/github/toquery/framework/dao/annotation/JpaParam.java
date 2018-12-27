package io.github.toquery.framework.dao.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.data.repository.query.Param;

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
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JpaParam {

    @AliasFor(attribute = "value", annotation = Param.class)
    String value();
}
