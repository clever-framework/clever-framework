package io.github.toquery.framework.dao.primary.generator;

import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.primary.snowflake.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.factory.internal.DefaultIdentifierGeneratorFactory;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

@Slf4j
public class AppSnowFlakeIdGenerator implements IdentifierGenerator, Configurable {

    private final SnowFlake snowFlake = new SnowFlake();

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        log.debug("AppSnowFlakeIdGenerator configure");
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        AppBaseEntity appBaseEntity = (AppBaseEntity) object;
        return (appBaseEntity != null && appBaseEntity.getId() != null && appBaseEntity.getId() != 0L) ? appBaseEntity.getId() : snowFlake.nextId();
    }
}
