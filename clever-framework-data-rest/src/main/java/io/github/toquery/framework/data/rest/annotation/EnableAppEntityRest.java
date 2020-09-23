package io.github.toquery.framework.data.rest.annotation;

import io.github.toquery.framework.data.rest.AppEntityRestFactoryBean;
import io.github.toquery.framework.data.rest.AppEntityRestScannerRegistrar;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
//@Import(AppEntityRestConfigurationSelector.class) // 代理模式选择器
@Import(AppEntityRestScannerRegistrar.class)
// @Repeatable(AppEntityRestScans.class)
public @interface EnableAppEntityRest {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation declarations e.g.:
     * {@code @AppEntityRestScan("org.my.pkg")} instead of {@code @AppEntityRestScan(basePackages = "org.my.pkg"})}.
     *
     * @return base package names
     */
    String[] value() default {};

    /**
     * Base packages to scan for MyBatis interfaces. Note that only interfaces with at least one method will be
     * registered; concrete classes will be ignored.
     *
     * @return base package names for scanning mapper interface
     */
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
    Class<? extends AppEntityRestFactoryBean> factoryBean() default AppEntityRestFactoryBean.class;


    /**
     * Whether enable lazy initialization of mapper bean.
     *
     * <p>
     * Default is {@code false}.
     * </p>
     *
     * @return set {@code true} to enable lazy initialization
     * @since 2.0.2
     */
    boolean lazyInitialization() default false;


    /**
     * Indicate how caching advice should be applied.
     * <p><b>The default is {@link AdviceMode#PROXY}.</b>
     * Please note that proxy mode allows for interception of calls through the proxy
     * only. Local calls within the same class cannot get intercepted that way;
     * a caching annotation on such a method within a local call will be ignored
     * since Spring's interceptor does not even kick in for such a runtime scenario.
     * For a more advanced mode of interception, consider switching this to
     * {@link AdviceMode#ASPECTJ}.
     */
    AdviceMode mode() default AdviceMode.PROXY;
}
