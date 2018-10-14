package io.github.toquery.framework.dao.jpa.lookup;

import com.google.common.collect.Maps;
import io.github.toquery.framework.dao.jpa.annotation.MybatisQuery;
import io.github.toquery.framework.dao.jpa.lookup.repository.MybatisRepositoryQuery;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
public class MybatisQueryLookupStrategy extends QueryLookupStrategyAdvice {

    @Getter
    private String name = "mybatis";

    /**
     * 存储dao接口和mybatis中mapper代理的映射
     */
    private static Map<String, Object> mybatisMapperMap = Maps.newHashMap();

    private static SqlSessionTemplate sqlSessionTemplate;

    public MybatisQueryLookupStrategy(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public boolean isEnabled(Method method, RepositoryMetadata metadata) {
        //方法包含mybatis的方法
        return method.getAnnotation(MybatisQuery.class) != null;
    }

    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {
        if (sqlSessionTemplate == null) {
            sqlSessionTemplate = new SqlSessionTemplate(beanFactory.getBean(SqlSessionFactory.class));
        }

        log.info("通过模板sqlSessionTemplate解析方法{}", method.getName());

        Assert.notNull(sqlSessionTemplate, "创建" + SqlSessionTemplate.class.getSimpleName() + "失败, 请初始化mybatis配置");

        Object mapper = null;
        //获取mybatis的mapper映射处理类
        if (!mybatisMapperMap.containsKey(getMapperKey(metadata))) {
            mapper = sqlSessionTemplate.getMapper(metadata.getRepositoryInterface());
            mybatisMapperMap.put(getMapperKey(metadata), mapper);
        } else {
            mapper = mybatisMapperMap.get(getMapperKey(metadata));
        }

        return new MybatisRepositoryQuery(mapper, method, metadata, factory);
    }

    /**
     * 获取mybatis中mapper的key
     *
     * @return
     */
    public String getMapperKey(RepositoryMetadata metadata) {
        return metadata.getRepositoryInterface().getName();
    }
}
