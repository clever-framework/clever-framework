package io.github.toquery.framework.web.dict.annotation;


import io.github.toquery.framework.web.dict.AppDictFactoryBean;
import io.github.toquery.framework.web.dict.AppDictScannerRegistrar;
import org.springframework.beans.factory.support.BeanNameGenerator;
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

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation declarations e.g.:
     * {@code @AppEntityRestScan("org.my.pkg")} instead of {@code @AppEntityRestScan(basePackages = "org.my.pkg"})}.
     *
     * @return base package names
     */
    @AliasFor("basePackages")
    String[] value() default {};



    /**
     * Base packages to scan for MyBatis interfaces. Note that only interfaces with at least one method will be
     * registered; concrete classes will be ignored.
     *
     * @return base package names for scanning mapper interface
     */
    @AliasFor("value")
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages to scan for annotated components. The
     * package of each class specified will be scanned.
     * <p>
     * Consider creating a special no-op marker class or interface in each package that serves no purpose other than being
     * referenced by this attribute.
     *
     * @return classes that indicate base package for scanning mapper interface
     */
    Class<?>[] basePackageClasses() default {};


    /**
     * The {@link BeanNameGenerator} class to be used for naming detected components within the Spring container.
     *
     * @return the class of {@link BeanNameGenerator}
     */
    Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;


    /**
     * Specifies a custom MapperFactoryBean to return a mybatis proxy as spring bean.
     *
     * @return the class of {@code AppEntityRestFactoryBean}
     */
    Class<? extends AppDictFactoryBean> factoryBean() default AppDictFactoryBean.class;


    /**
     * Whether enabled lazy initialization of mapper bean.
     *
     * <p>
     * Default is {@code false}.
     * </p>
     *
     * @return set {@code true} to enabled lazy initialization
     * @since 2.0.2
     */
    boolean lazyInitialization() default false;
}
