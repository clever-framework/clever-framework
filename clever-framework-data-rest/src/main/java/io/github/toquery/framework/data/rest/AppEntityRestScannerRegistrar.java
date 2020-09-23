package io.github.toquery.framework.data.rest;

import io.github.toquery.framework.data.rest.annotation.EnableAppEntityRest;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScannerRegistrar;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @see org.mybatis.spring.annotation.MapperScannerRegistrar
 */
@Slf4j
public class AppEntityRestScannerRegistrar implements ImportBeanDefinitionRegistrar {

    public AppEntityRestScannerRegistrar() {
        log.info(this.getClass().getName());
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        log.info("开始注册-动态MVC创建规则");
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableAppEntityRest.class.getName()));
        if (annotationAttributes == null) {
            throw new IllegalArgumentException("annotationAttributes EnableAppEntityRest not found");
        }

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(AppEntityRestScannerConfigurer.class);
        builder.addPropertyValue("processPropertyPlaceHolders", true);

        Class<? extends BeanNameGenerator> generatorClass = annotationAttributes.getClass("nameGenerator");
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            builder.addPropertyValue("nameGenerator", BeanUtils.instantiateClass(generatorClass));
        }

        Class<? extends AppEntityRestFactoryBean> mapperFactoryBeanClass = annotationAttributes.getClass("factoryBean");
        if (!AppEntityRestFactoryBean.class.equals(mapperFactoryBeanClass)) {
            builder.addPropertyValue("appEntityRestFactoryBean", mapperFactoryBeanClass);
        }


        boolean lazyInitialization = annotationAttributes.getBoolean("lazyInitialization");
        builder.addPropertyValue("lazyInitialization", lazyInitialization);

        Set<String> basePackages = new HashSet<>();
        basePackages.addAll(
                Arrays.stream(annotationAttributes.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toSet()));

        basePackages.addAll(Arrays.stream(annotationAttributes.getStringArray("basePackages")).filter(StringUtils::hasText)
                .collect(Collectors.toSet()));

        basePackages.addAll(Arrays.stream(annotationAttributes.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName)
                .collect(Collectors.toSet()));

        if (basePackages.isEmpty()) {
            basePackages.add(getDefaultBasePackage(annotationMetadata));
        }

        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));

        String beanName = generateBaseBeanName(annotationMetadata, 0);

        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());


        BeanDefinitionBuilder processBuilder = BeanDefinitionBuilder.genericBeanDefinition(AppEntityRestRequestHandlerMapping.class);
        processBuilder.addConstructorArgValue(basePackages);
        registry.registerBeanDefinition("contractAutoHandlerRegisterHandlerMapping", processBuilder.getBeanDefinition());
        log.info("结束注册-动态MVC创建规则");
    }

    private static String getDefaultBasePackage(AnnotationMetadata importingClassMetadata) {
        return ClassUtils.getPackageName(importingClassMetadata.getClassName());
    }

    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {
        return importingClassMetadata.getClassName() + "#" + MapperScannerRegistrar.class.getSimpleName() + "#" + index;
    }
}
