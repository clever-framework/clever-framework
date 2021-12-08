package io.github.toquery.framework.dao.jpa.listener;

import org.hibernate.bytecode.enhance.spi.LazyPropertyInitializer;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.internal.DefaultMergeEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.property.access.internal.PropertyAccessStrategyBackRefImpl;
import org.hibernate.type.Type;

import java.util.Map;

/**
 * jpa忽略空值的事件监听器
 */
public class IgnoreNullEventListener extends DefaultMergeEventListener {

    public static final IgnoreNullEventListener INSTANCE = new IgnoreNullEventListener();

    @Override
    protected void copyValues(EntityPersister persistent, Object entity, Object target, SessionImplementor source, Map copyCache) {
        //原始目标对象
        Object[] original = persistent.getPropertyValues(entity);
        //存储目标对象
        Object[] targets = persistent.getPropertyValues(target);

        Type[] types = persistent.getPropertyTypes();

        Object[] copied = new Object[original.length];

        for (int i = 0; i < types.length; i++) {
            // 如果是空值，则跳过
            if (original[i] == null || original[i] == LazyPropertyInitializer.UNFETCHED_PROPERTY || original[i] == PropertyAccessStrategyBackRefImpl.UNKNOWN) {
                copied[i] = targets[i];
            } else {
                copied[i] = types[i].replace(original[i], targets[i], source, target, copyCache);
            }
        }

        persistent.setPropertyValues(target, copied);
    }
}
