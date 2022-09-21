package io.github.toquery.framework.jpa.query.criteria.internal;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.CriteriaDeleteImpl;

/**
 * 继承Hibernate 的 CriteriaDeleteImpl 查询，构造方法公开
 *
 * @author toquery
 * @version 1
 */
public class CriteriaDeleteDefaultImpl<T> extends CriteriaDeleteImpl<T> {

    public CriteriaDeleteDefaultImpl(CriteriaBuilderImpl criteriaBuilder) {
        super(criteriaBuilder);
    }
}
