package io.github.toquery.framework.dao.support;

import java.util.Date;

/**
 * hibernate:
 *
 * @author toquery
 * @version 1
 * <br/>
 * hibernate:
 * @see org.hibernate.query.criteria.internal.predicate.ComparisonPredicate.ComparisonOperator
 * <br/>
 * Spring Data Jpa:
 * @see org.springframework.data.domain.Sort.Direction
 */
public enum AppDaoEnumOperator {
    EQ(Object.class, false), NEQ(Object.class, false),
    LIKE(String.class, false), LLIKE(String.class, false), RLIKE(String.class, false), NLIKE(String.class, false),
    GT(Comparable.class, false), LT(Comparable.class, false), GTE(Comparable.class, false), LTE(Comparable.class, false),
    EQDATE(Date.class, false), NEQDATE(Date.class, false), GTDATE(Date.class, false), LTDATE(Date.class, false), GTEDATE(Date.class, false), LTEDATE(Date.class, false),
    ISNULL(Object.class, true), ISNOTNULL(Object.class, true),
    IN(String.class, false), NOTIN(String.class, false), BOOLEANQE(Boolean.class, false);

    public Class applyClass;

    public boolean isAllowNullValue = false;

    AppDaoEnumOperator(Class applyClass, boolean isAllowNullValue) {
        this.applyClass = applyClass;
        this.isAllowNullValue = isAllowNullValue;
    }
}
