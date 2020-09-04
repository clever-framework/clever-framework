package io.github.toquery.framework.web.dict;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


/**
 * 自定义实例化Bean方式
 *
 * @param <T>
 */
public class AppFactoryBean<T> implements FactoryBean<T> {
    private String innerClassName;

    public void setInnerClassName(String innerClassName) {
        this.innerClassName = innerClassName;
    }


    public T getObject() throws Exception {
        Class innerClass = Class.forName(innerClassName);
        if (innerClass.isInterface()) {
            return (T) AppDictInvocationHandler.newInstance(innerClass);
        } else {
            Enhancer enhancer = new Enhancer();
            // enhancer.setSuperclass(innerClass);
            enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    System.out.println("MethodInterceptorImpl:" + method.getName());
                    return methodProxy.invokeSuper(o, objects);
                }
            });
            return (T) enhancer.create();
        }
    }

    public Class<?> getObjectType() {
        if (innerClassName == null) {
            return null;
        }
        try {
            return Class.forName(innerClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isSingleton() {
        return true;
    }

}


