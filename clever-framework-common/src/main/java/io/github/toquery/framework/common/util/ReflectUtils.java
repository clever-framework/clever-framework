package io.github.toquery.framework.common.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *
 */
public class ReflectUtils {


    /**
     * 安全返回数组某个下标的元素
     *
     * @param array 数组
     * @param index 下标
     * @param <T>   泛型
     * @return 某个元素
     */
    public static <T> T safeElement(T[] array, int index) {
        return array == null ? null : array.length - 1 < index ? null : array[index];
    }

    /**
     * 判断方法中是否包含某个类型的参数
     *
     * @param method     方法
     * @param paramClass 参数class
     * @return true/false
     */
    public static boolean checkMethodHasParamClass(Method method, Class paramClass) {
        Type[] types = method.getGenericParameterTypes();
        if (types != null && types.length > 0) {
            for (Type type : types) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    if (parameterizedType.getRawType().getTypeName().equals(paramClass.getName())) {
                        return true;
                    }
                } else {
                    if (type.getTypeName().equals(paramClass.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 通过反射获取方法某个参数的泛型
     *
     * @param method     方法
     * @param paramClass 参数class
     * @return 泛型
     */
    public static Type[] reflectMethodParameterTypes(Method method, Class paramClass) {
        if (!checkMethodHasParamClass(method, paramClass)) {
            return null;
        }
        Type[] types = method.getGenericParameterTypes();
        if (types != null && types.length > 0) {
            for (Type type : types) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    if (parameterizedType.getRawType().getTypeName().equals(paramClass.getName())) {
                        return parameterizedType.getActualTypeArguments();
                    }
                }
            }
        }
        return new Type[]{Object.class};
    }

    /**
     * 通过反射获取方法的返回参数的泛型
     *
     * @param method 方法
     * @return 泛型
     */
    public static Type[] reflectMethodReturnTypes(Method method) {
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments();
        }
        return new Type[]{Object.class};
    }

}
