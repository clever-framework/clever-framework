package io.github.toquery.framework.web.dict;

import io.github.toquery.framework.web.dict.annotation.AppDictScan;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

import java.util.Set;

public class AppDictScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        //获取所有注解的属性和值
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(AppDictScan.class.getName()));
        assert annotationAttributes != null;
        String[] basePackages = annotationAttributes.getStringArray("basePackages");
        //如果没有设置basePackage 扫描路径,就扫描对应包下面的值
        if(basePackages.length == 0){
            basePackages = new String[]{((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass().getPackage().getName()};
        }

        //自定义的包扫描器
        ClassPathAppDictScanner scanHandle = new ClassPathAppDictScanner(beanDefinitionRegistry,false);

        if(resourceLoader != null){
            scanHandle.setResourceLoader(resourceLoader);
        }
        //这里实现的是根据名称来注入
        scanHandle.setBeanNameGenerator(new AnnotationBeanNameGenerator());
        //扫描指定路径下的接口
        Set<BeanDefinitionHolder> beanDefinitionHolders = scanHandle.doScan(basePackages);
    }

}
