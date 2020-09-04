package com.toquery.framework.demo.scanner;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
/*
public class DefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0) throws BeansException {

    }

    *//**
     * 先执行postProcessBeanDefinitionRegistry方法
     * 在执行postProcessBeanFactory方法
     *//*
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathBeanDefinitionScanner scanner = new
                ClassPathBeanDefinitionScanner(registry);
        // bean 的名字生成规则在AnnotationBeanNameGenerator
        scanner.setBeanNameGenerator(new AnnotationBeanNameGenerator());
        // 设置哪些注解的扫描
        scanner.addIncludeFilter(new AnnotationTypeFilter(AppDict.class));
        // scanner.scan("com.github.toquery.framework");
        // 注意helloWord已经注册到容器中. 细看AnnotationBeanNameGenerator 的
    }
}*/
