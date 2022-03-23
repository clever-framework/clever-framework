package io.github.toquery.framework.dao.annotation;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.annotation.QueryAnnotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对mybatis查询方法和类的注解
 */
@Documented
@Mapper
@QueryAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface MybatisQuery {
}
