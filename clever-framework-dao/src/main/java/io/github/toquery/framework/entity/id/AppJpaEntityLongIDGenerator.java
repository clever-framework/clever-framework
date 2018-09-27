package io.github.toquery.framework.entity.id;

import com.google.common.collect.Maps;
import io.github.toquery.framework.id.snowflake.DockerSnowflakeIDPart;
import io.github.toquery.framework.id.snowflake.Snowflake2IDGenerator;
import io.github.toquery.framework.id.snowflake.SnowflakeIDPart;
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
    private static Snowflake2IDGenerator snowflake2IDGenerator = new Snowflake2IDGenerator(new DockerSnowflakeIDPart());

    private static final Map<String, SnowflakeIDPart> ID_PART_MAP = Maps.newHashMap();

    /**
     * 注册id的映射，可以根据不同的实体生成不同的id，便于以后分库和分表
     */
    public static void registerIdPart(String packagePrefix, SnowflakeIDPart snowflakeIDPart) {
        ID_PART_MAP.put(packagePrefix, snowflakeIDPart);
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
    }

    /**
     * 生成id
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        SnowflakeIDPart snowflakeIDPart = getSnowflakeIDPart(object);
        return snowflakeIDPart == null ? snowflake2IDGenerator.getNextId(object) : snowflake2IDGenerator.getNextId(snowflakeIDPart, object);
    }

    /**
     * 根据不同的对象获取不同的id生成方式
     */
    public SnowflakeIDPart getSnowflakeIDPart(Object object) {
        if (object == null) {
            return null;
        }
        String targetPackageName = object.getClass().getPackage().getName();

        SnowflakeIDPart targetSnowflakeIDPart = snowflake2IDGenerator.getSnowflakeIDPart();
        int packageLength = 0;

        for (String packageName : ID_PART_MAP.keySet()) {
            if (!targetPackageName.startsWith(packageName)) {
                continue;
            }
            //默认按照长度进行匹配
            if (packageName.length() >= packageLength) {
                targetSnowflakeIDPart = ID_PART_MAP.get(packageName);
                packageLength = packageName.length();
            }
        }

        log.debug("{} 使用\"{}\"生成id", object.getClass().getName(), targetSnowflakeIDPart.getName());
        ;

        return targetSnowflakeIDPart;
    }

}
