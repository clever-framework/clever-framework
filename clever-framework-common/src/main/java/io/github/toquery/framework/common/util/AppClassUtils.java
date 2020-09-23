package io.github.toquery.framework.common.util;

import com.fasterxml.jackson.databind.util.ClassUtil;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AppClassUtils {


    /**
     * 查找指定类注解Contract的接口
     * @param clazz 指定类
     * @return 返回该类标注了 Contract 的接口，如果未找到返回 null
     */
    public static Class<?> getAnnotationClass(Class<?> clazz, Class<? extends Annotation> annotationType) {
        //获取该Bean的所有接口
        Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(clazz);
        Class<?> contractMarkClass = null;
        //遍历接口找到注解了Contract的接口
        for (Class<?> anInterface : interfaces) {
            boolean isContractMarkClass  = AnnotatedElementUtils.hasAnnotation(anInterface, annotationType);
            if (isContractMarkClass) {
                contractMarkClass  = anInterface;
                break;
            }
        }
        return contractMarkClass;
    }

    /**
     * 判断指定类是否是Contract的代理类
     * @param clazz 要判断的指定类
     * @return 如果是 ContractTarget 返回 true，否则返回 false
     */
    public static boolean isContractTargetClass(Class<?> clazz, Class<? extends Annotation> annotationType) {
        Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(clazz);
        for (Class<?> anInterface : interfaces) {
            if (anInterface.equals(annotationType))
                return true;
        }
        return false;
    }

    /**
     * 执行无参数方法
     * @param beanType 查找方法的指定类
     * @param target 执行方法的目标类
     * @param methodName 方法名
     * @param <T>
     * @return 方法执行结果
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeNoParameterMethod(Class<?> beanType, Object target ,String methodName) {
        try {
            Method returnValueHandlersMethod = beanType.getDeclaredMethod(methodName);
            returnValueHandlersMethod.setAccessible(true);
            return (T) returnValueHandlersMethod.invoke(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取该MethodParameter对应Contract注解接口对应方法MethodParameter
     * @param parameter
     * @return

    public static MethodParameter getContractInterfaceMethodParameter(MethodParameter parameter) {
        Method method = parameter.getMethod();
        Class<?> declaringClass  = method.getDeclaringClass();
        Class<?> contractMarkClass  = ClassUtil.getContractMarkClass(declaringClass);
        if (contractMarkClass  == null)
            return null;

        try {
            Method originalMethod = contractMarkClass.getMethod(method.getName(), method.getParameterTypes());
            //根据此 Method 生成对应 MethodParameter 方法
            MethodParameter originalParameter = new MethodParameter(originalMethod, parameter.getParameterIndex());
            return originalParameter;
        } catch (Exception ex) {
            return null;
        }
    }*/
}
