package io.github.toquery.framework.data.rest;

import io.github.toquery.framework.common.util.AppClassUtils;
import io.github.toquery.framework.data.rest.annotation.AppEntityRest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

@Slf4j
public class AppEntityRestRequestHandlerMapping extends RequestMappingHandlerMapping {

    Set<String> basePackages;

    private RequestMappingInfo.BuilderConfiguration mappingInfoBuilderConfig;
    private boolean isGetSupperClassConfig = false;
    Boolean enable = false;


    public AppEntityRestRequestHandlerMapping(Set<String> basePackages) {
        super();
        this.basePackages = basePackages;
        // 这里为了能够比SimpleMappingHandlerMapping早点使用，不然会匹配/** 的url
        super.setOrder(super.getOrder() - 1);
//        String property = System.getProperty("concise-mvc.enable","true");
//        enable = new Boolean(property);
        this.enable = true;
    }


    /**
     * 判断是否符合触发自定义注解的实现类方法
     */
    @Override
    protected boolean isHandler(Class<?> beanType) {
        // 注解了 @Contract 的接口, 并且是这个接口的实现类
        if (!enable) {
            return false;
        }

        // 传进来的可能是接口，比如 FactoryBean 的逻辑
        if (beanType.isInterface()) {
            return false;
        }

        // 是否是Contract的代理类，如果是则不支持
        if (AppClassUtils.isContractTargetClass(beanType, AppEntityRest.class)) {
            return false;
        }

        // 是否在包范围内，如果不在则不支持
        if (!isPackageInScope(beanType)) {
            return false;
        }

        // 是否有标注了 @Contract 的接口
        Class<?> contractMarkClass = AppClassUtils.getAnnotationClass(beanType, AppEntityRest.class);
        log.info(beanType.getName());
        if (contractMarkClass != null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void initHandlerMethods() {
        super.initHandlerMethods();
        ApplicationContext applicationContext = super.getApplicationContext();


        Map<String, EntityManagerFactory> entityManagerMap1 = applicationContext.getBeansOfType(EntityManagerFactory.class);
        Map<String, EntityManager> entityManagerMap = applicationContext.getBeansOfType(EntityManager.class);
        applicationContext.getBeansOfType(EntityManager.class).get("org.springframework.orm.jpa.SharedEntityManagerCreator#0");

    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        Class<?> contractMarkClass = AppClassUtils.getAnnotationClass(handlerType, AppEntityRest.class);
        try {
            // 查找到原始接口的方法，获取其注解解析为 requestMappingInfo
            Method originalMethod = contractMarkClass.getMethod(method.getName(), method.getParameterTypes());
            RequestMappingInfo info = buildRequestMappingByMethod(originalMethod);

            if (info != null) {
                RequestMappingInfo typeInfo = buildRequestMappingByClass(contractMarkClass);
                if (typeInfo != null)
                    info = typeInfo.combine(info);
            }
            return info;
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

    private RequestMappingInfo buildRequestMappingByClass(Class<?> contractMarkClass) {

        String simpleName = contractMarkClass.getSimpleName();
        String[] paths = new String[]{simpleName};
        RequestMappingInfo.Builder builder = RequestMappingInfo.paths(resolveEmbeddedValuesInPatterns(paths));

        // 通过反射获得 config
        if (!isGetSupperClassConfig) {
            RequestMappingInfo.BuilderConfiguration config = getConfig();
            this.mappingInfoBuilderConfig = config;
        }

        if (this.mappingInfoBuilderConfig != null)
            return builder.options(this.mappingInfoBuilderConfig).build();
        else
            return builder.build();
    }

    private RequestMappingInfo buildRequestMappingByMethod(Method originalMethod) {
        String name = originalMethod.getName();
        String[] paths = new String[]{name};
        // 用名字作为url
        // post形式
        // json请求
        RequestMappingInfo.Builder builder = RequestMappingInfo.paths(resolveEmbeddedValuesInPatterns(paths))
                .methods(RequestMethod.POST);
//                .params(requestMapping.params())
//                .headers(requestMapping.headers())
//                .consumes(MediaType.APPLICATION_JSON_VALUE)
//                .produces(MediaType.APPLICATION_JSON_VALUE)
//                .mappingName(name);
        return builder.options(this.getConfig()).build();
    }

    RequestMappingInfo.BuilderConfiguration getConfig() {
        Field field = null;
        RequestMappingInfo.BuilderConfiguration configChild = null;
        try {
            field = RequestMappingHandlerMapping.class.getDeclaredField("config");
            field.setAccessible(true);
            configChild = (RequestMappingInfo.BuilderConfiguration) field.get(this);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            log.error(e.getMessage(), e);
        }
        return configChild;
    }

    /**
     * 判断指定类是否在包范围内
     *
     * @param beanType 指定类
     * @return 如果在范围内返回 true，否则返回 false
     */
    private boolean isPackageInScope(Class<?> beanType) {
        // 是否在包路径内
        String packageName = ClassUtils.getPackageName(beanType);
        boolean isPackageScope = false;
        for (String basePackage : basePackages) {
            if (packageName.startsWith(basePackage)) {
                isPackageScope = true;
                break;
            }
        }
        return isPackageScope;
    }

}
