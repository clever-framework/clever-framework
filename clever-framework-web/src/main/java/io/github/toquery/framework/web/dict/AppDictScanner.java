package io.github.toquery.framework.web.dict;

import io.github.toquery.framework.web.dict.annotation.AppDict;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

public final class AppDictScanner extends ClassPathBeanDefinitionScanner {

    public AppDictScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public AppDictScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    public AppDictScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
        super(registry, useDefaultFilters, environment);
    }

    public AppDictScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment, ResourceLoader resourceLoader) {
        super(registry, useDefaultFilters, environment, resourceLoader);
    }

//    @Override
//    protected void registerDefaultFilters() {
//        super.addIncludeFilter(new AnnotationTypeFilter(AppDict.class));
//        super.registerDefaultFilters();
//    }



    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
//        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
//        for (BeanDefinitionHolder holder : beanDefinitions) {
//            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
//            definition.getPropertyValues().add("innerClassName", definition.getBeanClassName());
//            definition.setBeanClass(AppFactoryBean.class);
//        }
// return beanDefinitions;

        //添加过滤条件，这里是只添加了@NRpcServer的注解才会被扫描到
        super.addIncludeFilter(new AnnotationTypeFilter(AppDict.class));
        //调用spring的扫描
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);

        for (BeanDefinitionHolder holder : beanDefinitionHolders) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
            definition.getPropertyValues().add("innerClassName", definition.getBeanClassName());
            definition.setBeanClass(AppFactoryBean.class);
        }

        return beanDefinitionHolders;
    }

//    public boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
//        return super.isCandidateComponent(beanDefinition) && beanDefinition.getMetadata()
//                .hasAnnotation(AppDict.class.getName());
//    }
}
