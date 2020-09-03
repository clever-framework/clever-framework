package io.github.toquery.framework.dao.jpa.lookup;

import com.google.common.collect.Maps;
import io.github.toquery.framework.dao.annotation.MybatisQuery;
import io.github.toquery.framework.dao.query.MybatisRepositoryQuery;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
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
        //包含自定义注解的方法 或者包含Mapper注解的方法
        return method.getAnnotation(MybatisQuery.class) != null || method.getAnnotation(Mapper.class) != null;
    }

    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {
        if (sqlSessionTemplate == null) {
            sqlSessionTemplate = new SqlSessionTemplate(beanFactory.getBean(SqlSessionFactory.class));
        }

        log.info("通过模板 sqlSessionTemplate 解析方法 {}", method.getName());

        Assert.notNull(sqlSessionTemplate, "创建 " + SqlSessionTemplate.class.getSimpleName() + " 失败, 请初始化mybatis配置");

        // 获取Mapper接口类
        String repositoryInterfaceName = getRepositoryInterfaceName(metadata);

        Object mapper = null;
        //获取mybatis的mapper映射处理类
        if (!mybatisMapperMap.containsKey(repositoryInterfaceName)) {
            mapper = sqlSessionTemplate.getMapper(metadata.getRepositoryInterface());
            mybatisMapperMap.put(repositoryInterfaceName, mapper);
        } else {
            mapper = mybatisMapperMap.get(repositoryInterfaceName);
        }

        return new MybatisRepositoryQuery(mapper, method, metadata, factory);
    }

    /**
     * 获取mybatis中mapper的key
     *
     * @param metadata metadata for repository interfaces.
     * @return 接口名称
     */
    public String getRepositoryInterfaceName(RepositoryMetadata metadata) {
        return metadata.getRepositoryInterface().getName();
    }
}
