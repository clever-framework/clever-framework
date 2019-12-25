package io.github.toquery.framework.dao.primary.snowflake;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@GeneratedValue(generator = "generatedkey")
@GenericGenerator(name = "id_generator_snowflake", strategy = "io.github.toquery.framework.dao.primary.generator.AppJpaEntityLongIDGenerator")

@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdSnowFlake {
}
