package io.github.toquery.framework.core.primary.generator;

import io.github.toquery.framework.core.entity.AppBaseEntity;
import io.github.toquery.framework.core.primary.snowflake.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

/**
 * Try IdentifierGenerator instead of IdentityGenerator.
 */
@Slf4j
public class AppSnowFlakeIdGenerator  implements IdentifierGenerator {

    private final SnowFlake snowFlake = new SnowFlake();

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        log.debug("AppSnowFlakeIdGenerator configure entity {}", params.get("entity_name"));
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        AppBaseEntity appBaseEntity = (AppBaseEntity) object;
        long id = (appBaseEntity != null && appBaseEntity.getId() != null && appBaseEntity.getId() != 0L) ? appBaseEntity.getId() : snowFlake.nextId();
        log.info("new id {} for entity {}", id, object.getClass().getName());
        return id;
    }
}
