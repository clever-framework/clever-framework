package io.github.toquery.framework.dao.jpa.lookup.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 基于Spring JPA RepositoryQuery 的 mybatis 查询 实现
 */
@Slf4j
public class MybatisRepositoryQuery implements RepositoryQuery {

    private Object mapper;

    private Method method;

    private RepositoryMetadata repositoryMetadata;

    private ProjectionFactory projectionFactory;

    public MybatisRepositoryQuery(Object mapper, Method method, RepositoryMetadata repositoryMetadata, ProjectionFactory projectionFactory) {
        this.mapper = mapper;
        this.method = method;
        this.repositoryMetadata = repositoryMetadata;
        this.projectionFactory = projectionFactory;

        log.info("初始化 MyBatis 数据查询 {} 的领域类 {}", repositoryMetadata.getRepositoryInterface().getName(), repositoryMetadata.getDomainType());
    }

    @Override
    public Object execute(Object[] parameters) {
        log.info("执行 {} . {} ，参数为 {} ", repositoryMetadata.getRepositoryInterface().getName(), method.getName(), Arrays.toString(parameters));
        Object result = null;
        try {
            Assert.isTrue(mapper != null, repositoryMetadata.getRepositoryInterface().getName() + "对应的Mapper为null");
            if (mapper != null) {
                result = method.invoke(mapper, parameters);
            }
        } catch (Exception e) {
            log.error("使用 mybatis 执行 mapper : {} 中方法 {} 失败", repositoryMetadata.getRepositoryInterface().getName(), method.getName());
            e.printStackTrace();
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }

        return result;
    }

    @Override
    public QueryMethod getQueryMethod() {
        return new QueryMethod(method, repositoryMetadata, projectionFactory);
    }

}
