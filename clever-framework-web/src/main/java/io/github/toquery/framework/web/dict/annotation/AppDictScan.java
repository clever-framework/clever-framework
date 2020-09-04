package io.github.toquery.framework.web.dict.annotation;


import io.github.toquery.framework.web.dict.AppDictScannerRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扫描注册 class 为 Bean
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(AppDictScannerRegistrar.class)// 这个是我们的关键，实际上也是由这个类来扫描的
@Documented
public @interface AppDictScan {

    @AliasFor("basePackage")
    String[] value() default "";

    @AliasFor("value")
    String[] basePackage() default "";
}
