package io.github.toquery.framework.config.jpa.ddl;

import org.hibernate.tool.schema.internal.DefaultSchemaFilterProvider;
import org.hibernate.tool.schema.spi.SchemaFilter;

/**
 * 对ddl schema的生成策略，定制化
 */
public class AppSchemaFilterProvider extends DefaultSchemaFilterProvider {

    @Override
    public SchemaFilter getCreateFilter() {
        return new AppSchemaFilter() ;
    }

    @Override
    public SchemaFilter getMigrateFilter() {
        return new AppSchemaFilter() ;
    }

}
