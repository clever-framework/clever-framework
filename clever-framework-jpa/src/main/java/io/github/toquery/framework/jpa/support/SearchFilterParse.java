package io.github.toquery.framework.jpa.support;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
public class SearchFilterParse {

    /**
     * 字段和操作符之间的分隔符
     */
    private static final String SEPARATOR = ":";

    /**
     * searchParams 中key的格式为 FIELDNAME:OPERATOR 或 CONNECTOR:FIELDNAME:OPERATOR
     * <p>
     * 例如：primary:EQ 或 AND:businessSystem.primary:EQ 将对当前实体属性businessSystem的id属性进行等值查询 ;
     *
     * @param searchParams 查询参数
     * @return 转换后的查询条件
     */
    public static LinkedHashMap<String, SearchFilter> parse(Map<String, Object> searchParams) {

        if (searchParams == null || searchParams.isEmpty()) {
            return null;
        }

        //使用有序的列表
        LinkedHashMap<String, SearchFilter> filters = null;

        for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
            // 过滤掉空值
            String key = entry.getKey();
            Object value = entry.getValue();

            // 拆分 operator 与 filedAttribute
            String[] names = StringUtils.split(key, SEPARATOR);

            //构造不同条件之间的连接符
            Predicate.BooleanOperator connector = null;

            //查询字段
            String fieldName = null;

            //根据名称获取操作标识
            AppDaoEnumOperator operator = null;

            String group = "";


            switch (names.length){
                case 2:{
                    //默认使用and连接符
                    connector = Predicate.BooleanOperator.AND;
                    fieldName = names[0];
                    //最后一位为操作比较符号
                    operator = AppDaoEnumOperator.valueOf(names[1]);
                    break;
                }case 3:{
                    //长度为3时，根据是否有连接符确定不同的格式
                    if (names[0].equalsIgnoreCase(Predicate.BooleanOperator.AND.name()) || names[0].equalsIgnoreCase(Predicate.BooleanOperator.OR.name())) {
                        connector = Predicate.BooleanOperator.valueOf(names[0]);
                        fieldName = names[1];
                        //最后一位为操作比较符号
                        operator = AppDaoEnumOperator.valueOf(names[2]);
                    } else {
                        connector = Predicate.BooleanOperator.AND;
                        fieldName = names[0];
                        operator = AppDaoEnumOperator.valueOf(names[1]);
                        group = names[2];
                    }
                    break;
                }case 4:{
                    connector = Predicate.BooleanOperator.valueOf(names[0]);
                    fieldName = names[1];
                    //最后一位为操作比较符号
                    operator = AppDaoEnumOperator.valueOf(names[2]);
                    //组名称
                    group = names[3];
                    break;
                }
                default:{
                    throw new IllegalArgumentException(key + " is not a valid search filter name");
                }
            }


            if (value == null || StringUtils.isBlank(value.toString())) {
                //操作标识的比较值不允许为null，则进行过滤
                if (!operator.isAllowNullValue) {
                    continue;
                } else {
                    value = null;
                }
            }

            //创建searchFilter
            SearchFilter filter = new SearchFilter(fieldName, value, operator, fieldName, group, connector);

            //查询sql所属的组名称
            // filter.group =;

            //初始化值
            if (filters == null) {
                filters = Maps.newLinkedHashMap();
            }

            filters.put(key, filter);
        }

        return filters;
    }
}
