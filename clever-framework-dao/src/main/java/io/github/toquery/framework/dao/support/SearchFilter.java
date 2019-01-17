package io.github.toquery.framework.dao.support;

import com.google.common.collect.Maps;
import lombok.ToString;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * DAO查询支持的查询连接符和比较条件
 */
@ToString
public class SearchFilter {
	/**
	 * 字段和操作符之间的分隔符
	 */
	private static final String SEPARATOR = ":" ;
	/**
	 * sql查询字段名称
	 */
	public String fieldName;
	/*
	 * 查询值
	 */
	public Object value;
	
	public AppDaoEnumOperator operator;
	/**
	 * 属性名称
	 */
	public String attributeName ;
	/**
	 * sql语句所属组名称
	 */
	public String group ;
	
	public AppDaoEnumConnector connector ;
	
	public SearchFilter(String fieldName, AppDaoEnumOperator operator, Object value , AppDaoEnumConnector connector) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
		//属性名称默认和查询字段相同
		this.attributeName = fieldName ;
		this.connector = connector ;
	}

	/**
	 * searchParams中key的格式为FIELDNAME:OPERATOR 或 CONNECTOR:FIELDNAME:OPERATOR。
	 * 例如：primary:EQ 或 AND:businessSystem.primary:EQ将对当前实体属性businessSystem的id属性进行等值查询 ;
	 */
	public static LinkedHashMap<String, SearchFilter> parse(LinkedHashMap<String, Object> searchParams) {
		
		if(MapUtils.isEmpty(searchParams)){
			return null ;
		}
		
		//使用有序的列表
		LinkedHashMap<String, SearchFilter> filters = null ;
		
		for (Entry<String, Object> entry : searchParams.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey() ;
			Object value = entry.getValue() ;
			
			// 拆分operator与filedAttribute
			String[] names = StringUtils.split(key, SEPARATOR);
			//构造不同条件之间的连接符
			AppDaoEnumConnector connector = null ;
			//查询字段
			String fieldName = null ;
			//根据名称获取操作标识
			AppDaoEnumOperator operator = null ;
			
			String group = "" ;
			
			if(names.length==2){
				//默认使用and连接符
				connector = AppDaoEnumConnector.AND ;
				fieldName = names[0] ;
				//最后一位为操作比较符号
				operator = AppDaoEnumOperator.valueOf(names[1]);
			}else if(names.length==3){
				//长度为3时，根据是否有连接符确定不同的格式
				if(names[0].equals("AND") || names[0].equals("OR") ){
					connector = AppDaoEnumConnector.valueOf(names[0]) ;
					fieldName = names[1] ;
					//最后一位为操作比较符号
					operator = AppDaoEnumOperator.valueOf(names[2]);
				}else{
					connector = AppDaoEnumConnector.AND ;
					fieldName = names[0] ;
					operator = AppDaoEnumOperator.valueOf(names[1]);
					group = names[2] ;
				}
				
			}else if(names.length==4){
				connector = AppDaoEnumConnector.valueOf(names[0]) ;
				fieldName = names[1] ;
				//最后一位为操作比较符号
				operator = AppDaoEnumOperator.valueOf(names[2]);
				//组名称
				group = names[3] ;
			}else{
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			}
			
			if (value==null || StringUtils.isBlank(value.toString())) {
				//操作标识的比较值不允许为null，则进行过滤
				if(!operator.isAllowNullValue){
					continue ;
				}else{
					value = null ;
				}
			}
			
			//创建searchFilter
			SearchFilter filter = new SearchFilter(fieldName, operator, value ,connector) ;
			//查询sql所属的组名称
			filter.group = group ;
			
			//初始化值
			if(filters==null){
				filters = Maps.newLinkedHashMap() ;
			}
			
			filters.put(key, filter);
		}
		
		return filters;
	}


}
