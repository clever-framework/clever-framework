package io.github.toquery.framework.jpa.jpa.support;

import io.github.toquery.framework.common.constant.AppCommonConstant;
import io.github.toquery.framework.jpa.support.AppDaoEnumOperator;
import io.github.toquery.framework.jpa.support.SearchFilter;
import io.github.toquery.framework.jpa.util.UtilEscape;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class AppSpecification<T> implements Specification<T> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final Class<T> entityClazz;

    private final LinkedHashMap<String, SearchFilter> filters;

    /**
     * 存储临时分组的map
     */
    private final Map<String, List<SearchFilter>> groupFilterMap = new LinkedHashMap<String, List<SearchFilter>>();
    /**
     * 关联查询的map，暂时存储关联查询语句
     */
    private Map<String, List<SearchFilter>> joinMap = new LinkedHashMap<String, List<SearchFilter>>();

    public AppSpecification(Class<T> entityClazz, LinkedHashMap<String, SearchFilter> filters) {
        this.entityClazz = entityClazz;
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (filters == null || filters.size() <= 0) {
            return null;
        }
        //按照查询条件进行分组
        for (Map.Entry<String, SearchFilter> filterEntry : filters.entrySet()) {
            if (groupFilterMap.containsKey(filterEntry.getValue().getGroup())) {
                groupFilterMap.get(filterEntry.getValue().getGroup()).add(filterEntry.getValue());
            } else {
                List<SearchFilter> searchFilters = new ArrayList<SearchFilter>();
                searchFilters.add(filterEntry.getValue());
                groupFilterMap.put(filterEntry.getValue().getGroup(), searchFilters);
            }
        }

        Predicate allPredicate = null;

        for (Map.Entry<String, List<SearchFilter>> filterGroupEntry : groupFilterMap.entrySet()) {
            Predicate predicate = null;
            Predicate.BooleanOperator connector = filterGroupEntry.getValue().get(0).getConnector();
            for (SearchFilter filter : filterGroupEntry.getValue()) {
                String[] attributeNames = filter.getFieldName().split("[\\.]");
                if (attributeNames.length > 2) {
                    throw new IllegalArgumentException(filter.getFieldName() + " 不支持超过2级的属性查询 ");
                }
                //对于关联属性特殊处理，层级属性之间用“.”连接
                if (attributeNames.length == 2) {
                    filter.setAttributeName(attributeNames[1]);

                    if (!joinMap.containsKey(attributeNames[0])) {
                        joinMap.put(attributeNames[0], new ArrayList<SearchFilter>());
                    }

                    joinMap.get(attributeNames[0]).add(filter);

                    continue;
                }
                predicate = joinPredicate(entityClazz, predicate, root.get(attributeNames[0]), filter, builder);
            }
            //构建整个查询条件
            allPredicate = joinPredicate(entityClazz, allPredicate, predicate, connector, builder);
        }
        //清理现有分组查询map
        groupFilterMap.clear();
//				构建关联查询
        boolean isPlural = buildJoin(joinMap, root, builder, entityClazz);
//				构建完关联关系之后，移除已有的关系
        joinMap.clear();
//				唯一性查询
        query.distinct(isPlural);

        return allPredicate;
    }

    /**
     * 连接两个查询条件
     *
     * @param existPredicate 已有的查询条件
     * @param expression     新查询条件表达式
     * @param filter         查询源信息
     * @param builder        查询条件构造器
     * @return
     */
    private static <T> Predicate joinPredicate(Class<T> entityClazz, Predicate existPredicate, Expression expression, SearchFilter filter, CriteriaBuilder builder) {
        //创建新的predicate
        Predicate newPredicate = createPredicate(entityClazz, expression, filter, builder);
        //根据条件连接符构造查询条件
        return joinPredicate(entityClazz, existPredicate, newPredicate, filter.getConnector(), builder);
    }

    /**
     * 构建查询条件
     *
     * @param existPredicate 已有查询
     * @param newPredicate   新构建的查询
     * @param connector      已有查询和新构建查询之间的连接符
     * @param builder        查询构建器CriteriaBuilder
     * @return 连接后的查询条件
     */
    private static <T> Predicate joinPredicate(Class<T> entityClazz, Predicate existPredicate, Predicate newPredicate, Predicate.BooleanOperator connector, CriteriaBuilder builder) {
        if (newPredicate == null) {
            return existPredicate;
        }

        if (existPredicate == null) {
            existPredicate = newPredicate;
        } else {
            //存在两个predicate，使用条件连接符连接两个条件
            switch (connector) {
                case OR:
                    existPredicate = builder.or(existPredicate, newPredicate);
                    break;
                default:
                    existPredicate = builder.and(existPredicate, newPredicate);
                    break;
            }
        }
        return existPredicate;
    }

    /**
     * 创建单个查询条件
     *
     * @param expression 待查询的属性表达式
     * @param filter
     * @param builder
     * @return
     */
    private static <T> Predicate createPredicate(Class<T> entityClazz, Expression expression, SearchFilter filter, CriteriaBuilder builder) {

        Predicate predicate = null;
        switch (filter.getOperator()) {
            case EQ:
                predicate = builder.equal(expression, getFormattedValue(entityClazz, filter));
                break;
            case NEQ:
                predicate = builder.notEqual(expression, getFormattedValue(entityClazz, filter));
                break;
            //字符串类型比较
            case LIKE:
                predicate = builder.like(expression, "%" + getFormattedValue(entityClazz, filter) + "%", '/');
                break;
            case LLIKE:
                predicate = builder.like(expression, "%" + getFormattedValue(entityClazz, filter), '/');
                break;
            case RLIKE:
                predicate = builder.like(expression, getFormattedValue(entityClazz, filter) + "%", '/');
                break;
            case NLIKE:
                predicate = builder.notLike(expression, "%" + getFormattedValue(entityClazz, filter) + "%", '/');
                break;
            //一般类型的比较处理
            case GT:
                predicate = builder.greaterThan(expression, (Comparable) getFormattedValue(entityClazz, filter));
                break;
            case LT:
                predicate = builder.lessThan(expression, (Comparable) getFormattedValue(entityClazz, filter));
                break;
            case GTE:
                predicate = builder.greaterThanOrEqualTo(expression, (Comparable) getFormattedValue(entityClazz, filter));
                break;
            case LTE:
                predicate = builder.lessThanOrEqualTo(expression, (Comparable) getFormattedValue(entityClazz, filter));
                break;
            //日期类型比较
            case EQDATE:
                predicate = builder.equal(expression, (Comparable) getFormattedValue(entityClazz, filter));
                break;
            case NEQDATE:
                predicate = builder.notEqual(expression, (Comparable) getFormattedValue(entityClazz, filter));
                break;
            case GTDATE:
                predicate = builder.greaterThan(expression, (Comparable) getFormattedValue(entityClazz, filter));
                break;
            case LTDATE:
                predicate = builder.lessThan(expression, (Comparable) getFormattedValue(entityClazz, filter));
                break;
            case GTEDATE:
                predicate = builder.greaterThanOrEqualTo(expression, (Comparable) getFormattedValue(entityClazz, filter));
                break;
            case LTEDATE:
                predicate = builder.lessThanOrEqualTo(expression, (Comparable) getFormattedValue(entityClazz, filter));
                break;
            //空值判断
            case ISNULL:
                predicate = builder.isNull(expression);
                break;
            case ISNOTNULL:
                predicate = builder.isNotNull(expression);
                break;
            case IN:
                //可以识别数组，可以迭代的几何或一般类型
                Object[] values = null;
                if (filter.getValue().getClass().isArray()) {
                    values = (Object[]) filter.getValue();
                } else if (Iterable.class.isAssignableFrom(filter.getValue().getClass())) {
                    values = StringUtils.join((Iterable) filter.getValue(), ",").split(",");
                } else if (ClassUtils.isPrimitiveOrWrapper(filter.getValue().getClass())) {
                    values = new Object[]{filter.getValue()};
                } else {
                    values = String.valueOf(filter.getValue()).split(",");
                }
                predicate = expression.in(values);
                break;

            case NOTIN:
                //可以识别数组，可以迭代的几何或一般类型
                Object[] notInValues = null;
                if (filter.getValue().getClass().isArray()) {
                    notInValues = (Object[]) filter.getValue();
                } else if (Iterable.class.isAssignableFrom(filter.getValue().getClass())) {
                    notInValues = StringUtils.join((Iterable) filter.getValue(), ",").split(",");
                } else if (ClassUtils.isPrimitiveOrWrapper(filter.getValue().getClass())) {
                    values = new Object[]{filter.getValue()};
                } else {
                    notInValues = String.valueOf(filter.getValue()).split(",");
                }
                predicate = expression.in(notInValues).not();
                break;

            case BOOLEANQE:
                boolean flag = false;

                if (filter.getValue() != null) {
                    if (filter.getValue() instanceof Boolean) {
                        flag = (Boolean) filter.getValue();
                    } else if (filter.getValue() instanceof Number) {
                        flag = ((Number) filter.getValue()).intValue() > 0;
                    } else if (filter.getValue() instanceof String) {
                        flag = StringUtils.equalsIgnoreCase("true", filter.getValue().toString());
                    }
                }

                predicate = flag ? builder.isTrue(expression) : builder.isFalse(expression);

                break;
        }

        return predicate;
    }


    /**
     * 构建关联关系。
     * 如果存在集合关联关系，查询时需要使用distinct，以免出现多条记录，
     * 使用distinct关键字后，order by的属性必须包含在select中。
     *
     * @param joinMap     查询条件
     * @param root        查找的连接源
     * @param builder     查询构造器
     * @param entityClazz 查询实体
     * @return 是否存在构建集合关联关系，如果存在，返回true，否则返回false。
     */
    private static <T> Boolean buildJoin(Map<String, List<SearchFilter>> joinMap, Root<T> root, CriteriaBuilder builder, Class<T> entityClazz) {
        boolean flag = false;
        for (Map.Entry<String, List<SearchFilter>> entry : joinMap.entrySet()) {
            Attribute attribute = root.getModel().getAttribute(entry.getKey());
            //构建内连接对象
            Join join = null;
            if (attribute instanceof SetAttribute) {
                join = root.join((SetAttribute) attribute, JoinType.INNER);
            } else if (attribute instanceof ListAttribute) {
                join = root.join((ListAttribute) attribute, JoinType.INNER);
            } else if (attribute instanceof CollectionAttribute) {
                join = root.join((CollectionAttribute) attribute, JoinType.INNER);
            } else if (attribute instanceof SingularAttribute) {
                join = root.join((SingularAttribute) attribute, JoinType.INNER);
            }
            if (join == null) {
                throw new IllegalArgumentException(entry.getKey() + " 无法获取get方法或不支持连接查询。");
            }
            //判断是否存在与集合的关联关系
            if (!flag && attribute instanceof PluralAttribute) {
                flag = true;
            }
            //构造关联查询条件
            Predicate predicate = null;
            for (SearchFilter filter : entry.getValue()) {
                predicate = joinPredicate(entityClazz, predicate, join.get(filter.getAttributeName()), filter, builder);
            }
            //设置关联条件
            join.on(predicate);
        }
        return flag;
    }


    /**
     * 获取格式化之后的查询值
     *
     * @param filter
     * @return
     */
    public static <T> Object getFormattedValue(final Class<T> entityClazz, SearchFilter filter) {

        //获取所有的字段，包括父类中的字段
        // Set<Field> fields = ReflectionUtils.getAllFields(entityClazz, ReflectionUtils.withName(filter.getFieldName()));
        Set<Field> fields = FieldUtils.getAllFieldsList(entityClazz).stream().filter(item -> item.getName().equalsIgnoreCase(filter.getFieldName())).collect(Collectors.toSet());

        Field field = fields != null && fields.size() > 0 ? fields.toArray(new Field[]{})[0] : null;

        //获取字段类型
        Class<?> fieldClass = field != null ? field.getType() : null;

        Object value = getFormattedValue(fieldClass, filter.getOperator(), filter.getValue());

        log.debug("查询字段->" + filter.getFieldName() + " , 查询值-> " + value);

        return value;
    }

    /**
     * 获取格式化之后的查询值
     *
     * @param operator 操作符
     * @param value    操作值
     * @return
     */
    public static Object getFormattedValue(Class<?> fieldClass, AppDaoEnumOperator operator, Object value) {
        //过滤空值
        if (value == null || value.toString().equals("null")) {
            return value;
        }

        if (fieldClass != null && fieldClass.isEnum() && value instanceof String) {
            // 将枚举类的字符串转换成枚举对象
            try {
                value = EnumUtils.getEnum((Class<? extends Enum>) fieldClass, value.toString().trim());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (operator.applyClass.getName().equals(Date.class.getName()) && !(value instanceof Date)) {
            //将字符串转换为日期
            String dateString = value.toString().trim();
            String[] datePatterns = {AppCommonConstant.DATE_PATTERN, AppCommonConstant.DATE_TIME_PATTERN, AppCommonConstant.TIME_PATTERN};
            try {
                value = DateUtils.parseDate(dateString, datePatterns);
            } catch (ParseException e) {
                log.error("解析日期格式错误。" + e.getMessage());
            }

        } else if (operator.applyClass.getName().equals(String.class.getName())) {
            //为特殊字符加上转义字符
            value = UtilEscape.escapeSQL(value.toString());
        }

        return value;
    }
}
