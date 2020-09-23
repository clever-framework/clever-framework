package io.github.toquery.framework.data.rest;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * @see org.mybatis.spring.mapper.ClassPathMapperScanner
 */
@Slf4j
public class ClassPathAppEntityScanner extends ClassPathBeanDefinitionScanner {


    @Setter
    private boolean lazyInitialization;

    private Class<? extends Annotation> annotationClass;

    private Class<? extends AppEntityRestFactoryBean> appEntityRestFactoryBeanClass = AppEntityRestFactoryBean.class;

    public ClassPathAppEntityScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }


    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            log.warn("No MyBatis mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            log.debug("Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '" + beanClassName + "' mapperInterface");

            // the mapper interface is the original class of the bean
            // but, the actual class of the bean is MapperFactoryBean
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName); // issue #59
            definition.setBeanClass(this.appEntityRestFactoryBeanClass);
            definition.getPropertyValues().add("appEntityClass", definition.getBeanClass());
            definition.setLazyInit(this.lazyInitialization);
        }
    }


    public void setAppEntityRestFactoryBeanClass(Class<? extends AppEntityRestFactoryBean> appEntityRestFactoryBeanClass) {
        this.appEntityRestFactoryBeanClass = appEntityRestFactoryBeanClass == null ? AppEntityRestFactoryBean.class : appEntityRestFactoryBeanClass;
    }

}
