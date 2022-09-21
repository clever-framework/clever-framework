package io.github.toquery.framework.jpa.jpa.support;

import io.github.toquery.framework.jpa.support.SearchFilter;
import io.github.toquery.framework.jpa.support.SearchFilterParse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.LinkedHashMap;

@Slf4j
public class DynamicJPASpecifications {

    /**
     * 构建jpa查询所需要的Specification对象
     *
     * @param searchParams 查询参数，格式要能够被SearchFilter解析
     * @param entityClazz  查询的目标对象
     * @return jpa查询需要的Specification对象
     */
    public static <T> Specification<T> bySearchParam(LinkedHashMap<String, Object> searchParams, Class<T> entityClazz) {
        LinkedHashMap<String, SearchFilter> filters = SearchFilterParse.parse(searchParams);
        return bySearchFilter(filters, entityClazz);
    }

    /**
     * 构建查询条件
     *
     * @param filters     被SearchFilter解析过的查询条件
     * @param entityClazz 查询的目标对象
     * @return jpa查询需要的Specification对象
     */
    public static <T> Specification<T> bySearchFilter(LinkedHashMap<String, SearchFilter> filters, Class<T> entityClazz) {
        return bySearchFilter(filters, entityClazz, false);
    }


    /**
     * 构建支持缓存的查询对象
     *
     * @param filters     被SearchFilter解析过的查询条件
     * @param entityClazz 查询的目标对象
     * @param cache       是否缓存查询结果
     * @return 查询对象
     */
    public static <T> Specification<T> bySearchFilter(LinkedHashMap<String, SearchFilter> filters, Class<T> entityClazz, boolean cache) {
        return new AppSpecification<T>(entityClazz, filters);
    }
}
