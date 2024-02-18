package io.github.toquery.framework.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
public class AppEntityUtils {

    /**
     * 利用org.apache.commons.beanutils 工具类实现 Map 转为 Bean
     *
     * @param map          需要转换的map
     * @param entityObject 实体对象
     * @param <T>          实体对象类型
     * @return 实体对象

    public static <T> T map2Entity2(Map<String, Object> map, T entityObject) {
        if (map == null || entityObject == null) {
            return null;
        }
        try {
            BeanUtils.populate(entityObject, map);
        } catch (Exception e) {
            System.err.println("map2Entity2 Error \n" + e);
        }
        return entityObject;
    }
     */

    /**
     * 利用Introspector,PropertyDescriptor实现 Map 转为 Bean
     *
     * @param map          需要转换的map
     * @param entityObject 实体对象
     * @param <T>          实体对象类型
     * @return 实体对象
     */
    public static <T> T map2Entity(Map<String, Object> map, T entityObject) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(entityObject.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(entityObject, value);
                }
            }
        } catch (Exception e) {
            System.err.println("map2Entity Error\n" + e);
        }
        return entityObject;

    }

    /**
     * 利用Introspector和PropertyDescriptor 将Bean 转为 Map
     *
     * @param entityObject 需要转换的实体对象
     * @return 返回的Map
     */
    public static Map<String, Object> entity2Map(Object entityObject) {
        if (entityObject == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(entityObject.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(entityObject);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            System.err.println("transBean2Map Error " + e);
        }
        return map;
    }
}
