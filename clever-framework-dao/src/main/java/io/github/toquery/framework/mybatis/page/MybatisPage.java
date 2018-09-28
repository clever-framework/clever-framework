package io.github.toquery.framework.mybatis.page;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * mybatis的分页接口，必须实现List。
 * Mybatis的接口会匹配接口方法返回值的类型和返回记录的条数，
 * 如果是返回值有多条，函数返回值类型必须为集合接口。
 * @param <T>
 */
public interface MybatisPage<T> extends Page<T>, List<T> {

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

}
