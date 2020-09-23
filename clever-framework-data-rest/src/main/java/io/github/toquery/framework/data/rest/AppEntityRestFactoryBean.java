package io.github.toquery.framework.data.rest;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @param <T>
 * @see org.mybatis.spring.mapper.MapperFactoryBean
 */
@Slf4j
public class AppEntityRestFactoryBean<T> implements FactoryBean<T> {

    @Setter
    @Getter
    private Class<T> appEntityClass;

    public AppEntityRestFactoryBean() {
        log.info(this.getClass().getName());
    }

    @Override
    public T getObject() throws Exception {
        if (appEntityClass == null || appEntityClass.isInterface()){
            throw new RuntimeException("App Entity Class 为空");
        }
        Enhancer enhancer = new Enhancer();
        // enhancer.setSuperclass(innerClass);
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object object, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("MethodInterceptorImpl:" + method.getName());
                return methodProxy.invokeSuper(object, objects);
            }
        });
        return (T) enhancer.create();
    }

    @Override
    public Class<?> getObjectType() {
        if (this.appEntityClass == null) {
            return null;
        }
        return appEntityClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
