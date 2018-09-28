package io.github.toquery.framework.config.jpa.ddl;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;
import org.hibernate.tool.schema.internal.DefaultSchemaFilter;

import java.util.Iterator;

/**
 * 数据库存储，除主键外的其它字段： 允许为空、不允许唯一
 */
@Slf4j
public class AppSchemaFilter extends DefaultSchemaFilter {

    @Override
    public boolean includeTable(Table table) {

        //数据库存储，除主键外的其它字段： 允许为空、不允许唯一
        Iterator iterator = table.getColumnIterator();
        while (iterator.hasNext()) {
            Column column = (Column) iterator.next();
            if (table.getPrimaryKey().getColumns().contains(column)) {
                log.info("table:{} 的主键: {} ", column.getName(), Joiner.on(",").join(table.getPrimaryKey().getColumns()));
            } else {
                column.setNullable(true);
                column.setUnique(false);
                if (column.isUnique() || !column.isNullable()) {
                    log.info("在应用服务中执行表格{}中列{}的有效性校验", table.getName(), column.getName());
                }
            }
        }

        //清空所有的外检
        if (table.getForeignKeys() != null && table.getForeignKeys().size() > 0) {
            log.error("清理table: {} 的外键设置，不允许存在实际的外键关联限制", table.getName());
//            todo
            throw new RuntimeException("不允许存在实际的外键关联限制");//Exception("不允许存在实际的外键关联限制") ;
        }

        return true;
    }

}
