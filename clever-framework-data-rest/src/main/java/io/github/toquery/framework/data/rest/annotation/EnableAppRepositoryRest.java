package io.github.toquery.framework.data.rest.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author toquery
 * @version 1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Inherited
//@Import(ContractAutoHandlerRegisterConfiguration.class)
public @interface EnableAppRepositoryRest {

    String[] basePackages() default {};
}
