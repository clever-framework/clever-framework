package io.github.toquery.framework.dao.support;

/**
 * 数据库操作连接符 AND\OR
 *
 * 已被废弃，使用JPA已有连接符 javax.persistence.criteria.Predicate.BooleanOperator
 *
 * @author toquery
 * @version 1
 * @deprecated
 */
@Deprecated
public enum AppDaoEnumConnector {
    AND, OR
}
