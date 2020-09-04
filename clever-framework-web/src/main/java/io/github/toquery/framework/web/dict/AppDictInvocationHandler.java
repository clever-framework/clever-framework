package io.github.toquery.framework.web.dict;

import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

import java.lang.reflect.Method;

public class AppDictInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
        return method.invoke(object, args);
    }

    public static <T> T newInstance(Class<T> innerInterface) {
        ClassLoader classLoader = innerInterface.getClassLoader();
        Class[] interfaces = new Class[]{innerInterface};
        AppDictInvocationHandler proxy = new AppDictInvocationHandler();
        return (T) Proxy.newProxyInstance(classLoader, interfaces, proxy);
    }
}
