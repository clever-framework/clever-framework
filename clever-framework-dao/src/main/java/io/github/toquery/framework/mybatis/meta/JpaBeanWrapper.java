package io.github.toquery.framework.mybatis.meta;

import com.google.common.collect.Maps;
import io.github.toquery.framework.mybatis.mapper.EntityField;
import io.github.toquery.framework.mybatis.mapper.FieldHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;

import java.util.Map;

/**
 * JPA实体查找
 */
@Slf4j
public class JpaBeanWrapper extends BeanWrapper {

    /**
     * class类和内部属性的映射
     */
    private static final Map<String , Map<String,EntityField>> entityFieldsMap = Maps.newConcurrentMap() ;

    protected Object object ;

    protected MetaClass metaClass ;

    //object class内的字段映射
    protected Map<String, EntityField> entityFieldColumnMap = Maps.newConcurrentMap() ;

    public JpaBeanWrapper(MetaObject metaObject, Object object) {
        super(metaObject, object) ;
        this.object = object;
        this.metaClass = MetaClass.forClass(object.getClass(), metaObject.getReflectorFactory());

        //如果没有当前实体的映射关系，则建立关系并保存
        if(!entityFieldsMap.containsKey(object.getClass().getName())){
            entityFieldsMap.put(object.getClass().getName() ,
                    FieldHelper.getAllEntityFieldColumnMap(object.getClass()) ) ;
        }

        entityFieldColumnMap = entityFieldsMap.get( object.getClass().getName() );
    }

    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        String property = null ;

        if(entityFieldColumnMap!=null && entityFieldColumnMap.get(name.trim().toLowerCase())!=null){
            property = entityFieldColumnMap.get(name.trim().toLowerCase()).getName() ;
            log.debug("列{}使用jpa注解映射实体属性{}" , name , property) ;
        }

        //使用默认的方式获取映射的属性
        if(StringUtils.isBlank(property)){
            property = super.findProperty(name , useCamelCaseMapping) ;

            log.debug("列{}使用mybatis实体映射方式获取{}属性为{}" , name , object.getClass().getName() , property); ;
        }

        return property ;
    }

}
