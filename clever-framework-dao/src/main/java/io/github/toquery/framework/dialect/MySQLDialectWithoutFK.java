package io.github.toquery.framework.dialect;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * 禁止mysql使用外键创建索引
 */
@Slf4j
public class MySQLDialectWithoutFK extends MySQL5InnoDBDialect {
    @Override
    public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey, String referencedTable, String[] primaryKey, boolean referencesPrimaryKey) {
        String fksql = super.getAddForeignKeyConstraintString(constraintName, foreignKey, referencedTable, primaryKey, referencesPrimaryKey);
        log.warn("禁止创建外键索引 {}", fksql);
        return "";
    }
}
