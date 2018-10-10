package io.github.toquery.framework.primary.generator;

import com.google.common.collect.Maps;
import io.github.toquery.framework.primary.snowflake.DockerSnowflakePrimaryKey;
import io.github.toquery.framework.primary.snowflake.Snowflake2PrimaryKeyGenerator;
import io.github.toquery.framework.primary.snowflake.SnowflakePrimaryKey;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

/**
 * 新的id生成器
 */
@Slf4j
public class AppJpaEntityLongIDGenerator implements IdentifierGenerator, Configurable {

    //公用一个id生成器
    private static Snowflake2PrimaryKeyGenerator snowflake2IDGenerator = new Snowflake2PrimaryKeyGenerator(new DockerSnowflakePrimaryKey());

    private static final Map<String, SnowflakePrimaryKey> ID_PART_MAP = Maps.newHashMap();

    /**
     * 注册id的映射，可以根据不同的实体生成不同的id，便于以后分库和分表
     */
    public static void registerIdPart(String packagePrefix, SnowflakePrimaryKey snowflakePrimaryKey) {
        ID_PART_MAP.put(packagePrefix, snowflakePrimaryKey);
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
    }

    /**
     * 生成id
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        SnowflakePrimaryKey snowflakePrimaryKey = getSnowflakeIDPart(object);
        return snowflakePrimaryKey == null ? snowflake2IDGenerator.getNextId(object) : snowflake2IDGenerator.getNextId(snowflakePrimaryKey, object);
    }

    /**
     * 根据不同的对象获取不同的id生成方式
     */
    public SnowflakePrimaryKey getSnowflakeIDPart(Object object) {
        if (object == null) {
            return null;
        }
        String targetPackageName = object.getClass().getPackage().getName();

        SnowflakePrimaryKey targetSnowflakePrimaryKey = snowflake2IDGenerator.getSnowflakePrimaryKey();
        int packageLength = 0;

        for (String packageName : ID_PART_MAP.keySet()) {
            if (!targetPackageName.startsWith(packageName)) {
                continue;
            }
            //默认按照长度进行匹配
            if (packageName.length() >= packageLength) {
                targetSnowflakePrimaryKey = ID_PART_MAP.get(packageName);
                packageLength = packageName.length();
            }
        }

        log.debug("{} 使用\"{}\"生成id", object.getClass().getName(), targetSnowflakePrimaryKey.getName());
        ;

        return targetSnowflakePrimaryKey;
    }

}
