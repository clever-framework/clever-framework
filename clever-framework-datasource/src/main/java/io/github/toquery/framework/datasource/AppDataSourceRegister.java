package io.github.toquery.framework.datasource;

import io.github.toquery.framework.datasource.properties.AppDataSourcesProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 *
 */
@Slf4j
public class AppDataSourceRegister implements EnvironmentAware, ImportBeanDefinitionRegistrar {

    private static final Map<String, Object> registerBean = new ConcurrentHashMap<>();


    private Environment env;
    private Binder binder;


    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
        // bing binder
        binder = Binder.get(this.env);
    }

    /**
     * ImportBeanDefinitionRegistrar
     *
     * @param annotationMetadata     annotationMetadata
     * @param beanDefinitionRegistry beanDefinitionRegistry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        // get all datasource
        Map<String, Map> multipleDataSources;
        try {
            multipleDataSources = binder.bind(AppDataSourcesProperties.PREFIX, Map.class).get();
        } catch (NoSuchElementException e) {
            log.error("Failed to configure fastDep DataSource: 'fastdep.datasource' attribute is not specified and no embedded datasource could be configured.");
            return;
        }
        for (String key : multipleDataSources.keySet()) {
            DataSource fastDepDataSource = binder.bind("fastdep.datasource." + key, DataSource.class).get();
            Supplier<DataSource> dataSourceSupplier;


            dataSourceSupplier = () -> {
                // fix xa not support
                DataSource registerDataSource = (DataSource) registerBean.get(key + "DataSource");
                if (registerDataSource != null) {
                    return registerDataSource;
                }
                registerDataSource = fastDepDataSource;
                registerBean.put(key + "DataSource", registerDataSource);
                return registerDataSource;
            };
            DataSource dataSource = dataSourceSupplier.get();
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DataSource.class, dataSourceSupplier);
            AbstractBeanDefinition datasourceBean = builder.getRawBeanDefinition();
            beanDefinitionRegistry.registerBeanDefinition(key + "DataSource", datasourceBean);


            SqlSessionFactory sqlSessionFactory = null;// sqlSessionFactorySupplier.get();
            BeanDefinitionBuilder builder2 =  null;//BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactory.class, sqlSessionFactorySupplier);
            BeanDefinition sqlSessionFactoryBean = builder2.getRawBeanDefinition();
            beanDefinitionRegistry.registerBeanDefinition(key + "SqlSessionFactory", sqlSessionFactoryBean);
            // sqlSessionTemplate
            GenericBeanDefinition sqlSessionTemplate = new GenericBeanDefinition();
            sqlSessionTemplate.setBeanClass(SqlSessionTemplate.class);
            ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
            constructorArgumentValues.addIndexedArgumentValue(0, sqlSessionFactory);
            sqlSessionTemplate.setConstructorArgumentValues(constructorArgumentValues);
            beanDefinitionRegistry.registerBeanDefinition(key + "SqlSessionTemplate", sqlSessionTemplate);
            // MapperScanner
            ClassPathMapperScanner scanner = new ClassPathMapperScanner(beanDefinitionRegistry);
            scanner.setSqlSessionTemplateBeanName(key + "SqlSessionTemplate");
            scanner.registerFilters();
            String mapperProperty = env.getProperty("fastdep.datasource." + key + ".mapper");
            if (mapperProperty == null) {
                log.error("Failed to configure fastDep dataSource: fastdep.datasource." + key + ".mapper cannot be null.");
                return;
            }
            scanner.doScan(mapperProperty);
            log.info("Registration dataSource ({}DataSource) !", key);
        }
        log.info("Registration dataSource completed !");
    }
}
