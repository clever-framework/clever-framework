package io.github.toquery.framework.entity.id;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.io.Serializable;

/**
 * 新的id生成器，生成string格式的id
 */
@Slf4j
public class AppJpaEntityStringIDGenerator extends AppJpaEntityLongIDGenerator {
    /**
     * 生成id
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return super.generate(session, object).toString();
    }

}
