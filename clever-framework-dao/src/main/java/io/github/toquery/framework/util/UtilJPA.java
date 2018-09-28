package io.github.toquery.framework.util;

import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JPA帮助类，主要包括如下方法：<p></p>
 * 1.获取实体类中所有简单映射方法 <br>
 */
public class UtilJPA {
    /**
     * 简单映射类型
     */
    public static Class[] simpleMapperClasses = {String.class, Date.class};

    /**
     * 获取实体中所有简单的数据库映射字段
     */
    public static List<String> getSimpleORMFieldInEntity(Class entityClass) {
        if (entityClass == null) {
            return null;
        }
        //获取所有声明的字段
        Set<Field> fields = ReflectionUtils.getAllFields(entityClass);
        List<String> simpleFields = new ArrayList<String>();
        for (Field field : fields) {
            if (isSimpleORMField(field)) {
                simpleFields.add(field.getName());
            }
        }
        return simpleFields;
    }

    /**
     * 判断字段是否为简单类型的映射
     */
    public static boolean isSimpleORMField(Field field) {
        boolean flag = Collection.class.isAssignableFrom(field.getType()) ?
                false : Map.class.isAssignableFrom(field.getType()) ?
                false : true;
        return flag;
    }
}
