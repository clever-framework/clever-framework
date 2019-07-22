package io.github.toquery.framework.dao.support;

/**
 * 数据库操作连接符 AND\OR
 * <p>
 * 已被废弃，使用JPA已有连接符
 *
 * @author toquery
 * @version 1
 * @see javax.persistence.criteria.Predicate.BooleanOperator
 * @deprecated 1.0.6
 */
@Deprecated
public enum AppDaoEnumConnector {
    AND, OR
}
