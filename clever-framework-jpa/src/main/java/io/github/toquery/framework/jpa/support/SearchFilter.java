package io.github.toquery.framework.jpa.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.criteria.Predicate;

/**
 * DAO查询支持的查询连接符和比较条件
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilter {

    /**
     * sql查询字段名称
     */
    private String fieldName;
    /*
     * 查询值
     */
    private Object value;

    private AppDaoEnumOperator operator;
    /**
     * 属性名称(字段名称)
     *
     * @see SearchFilter#fieldName
     */
    private String attributeName;
    /**
     * sql语句所属组名称
     */
    private String group;

    private Predicate.BooleanOperator connector;

    public SearchFilter(String fieldName, AppDaoEnumOperator operator, Object value, Predicate.BooleanOperator connector) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
        //属性名称默认和查询字段相同
        this.attributeName = fieldName;
        this.connector = connector;
    }

}
