package io.github.toquery.framework.common.util;

import com.google.common.base.Strings;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.DigestUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author
 */
public class Utils {
    public static final int MINUTE = 60;
    public static final int HOUR = MINUTE * 60;

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static int random(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 根据手机号生成用户名
     *
     * @param phone 手机号
     * @return 用户名
     */
    public static String generateNameByPhone(String phone) {
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 生成短信验证码
     *
     * @return 短信验证码
     */
    public static String generateSmsCode() {
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }

    public static Class getSuperClassGenericType(final Class clazz, final int index) {
        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 生成md5
     *
     * @param str 要加密的内容
     * @return 加密后内容
     */
    public static String md5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    /**
     * bean转map
     *
     * @param bean 原始bean
     * @param <T>  泛型
     * @return map
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将map装换为javabean对象
     *
     * @param map  原始map
     * @param bean 实例化空bean
     * @param <T>  泛型
     * @return 填充后的bean
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 排序签名
     *
     * @param o      要签名的对象
     * @param <T>    泛型
     * @param secret 秘钥
     * @return 加密后字符串
     */
    public static <T> String signWithSort(T o, String secret) {
        Map<String, Object> map = beanToMap(o);
        String signStr = map.keySet().stream().sorted()
                .map(key -> {
                    Object value = map.get(key);
                    if (value == null) {
                        return null;
                    }
                    return key.concat("=").concat(value + "");
                }).filter(Objects::nonNull)
                .collect(Collectors.joining("&"));
        return md5(signStr + secret);
    }

    /**
     * 获取文件名后缀，携带小数点
     *
     * @param fileName 文件名称
     * @return 后缀名称
     */
    public static String fileSuffixWithPoint(String fileName) {
        if (Strings.isNullOrEmpty(fileName)) {
            return "";
        }
        int suffixIndex = fileName.lastIndexOf(".");
        if (suffixIndex > -1) {
            return fileName.substring(suffixIndex);
        }
        return "";
    }

    /**
     * 获取文件名后缀，不携带小数点
     *
     * @param fileName 文件名称
     * @return 后缀名称
     */
    public static String fileSuffix(String fileName) {
        if (Strings.isNullOrEmpty(fileName)) {
            return "";
        }
        int suffixIndex = fileName.lastIndexOf(".");
        if (suffixIndex > -1) {
            return fileName.substring(suffixIndex + 1);
        }
        return "";
    }

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
