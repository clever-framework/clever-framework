package io.github.toquery.framework.data.rest.annotation;


import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// todo
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AppEntityRest {

    String path() default "";


    boolean exported() default true;

    String rel() default "";

    Description description() default @Description(value = "");

    /**
     * The rel value to use when generating links to the collection resource.
     *
     * @return A valid rel value.
     */
    String collectionResourceRel() default "";

    /**
     * The description of the collection resource.
     *
     * @return
     */
    Description collectionResourceDescription() default @Description(value = "");

    /**
     * The rel value to use when generating links to the item resource.
     *
     * @return A valid rel value.
     */
    String itemResourceRel() default "";

    /**
     * The description of the item resource.
     *
     * @return
     */
    Description itemResourceDescription() default @Description(value = "");

    /**
     * Configures the projection type to be used when embedding item resources into collections and related resources.
     * Defaults to {@link RepositoryRestResource.None}, which indicates full rendering of the items in a collection resource and no inlining of
     * related resources.
     *
     * @return
     */
    Class<?> excerptProjection() default RepositoryRestResource.None.class;

    static class None {}
}
