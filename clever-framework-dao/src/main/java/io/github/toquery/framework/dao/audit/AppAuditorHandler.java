package io.github.toquery.framework.dao.audit;

import io.github.toquery.framework.dao.entity.AppBaseEntity;
import org.springframework.core.Ordered;


/**
 * @author toquery
 * @version 1
 */
public interface AppAuditorHandler extends Ordered {

    boolean enable();

    public void onPrePersist(AppBaseEntity appBaseEntity);

    public void onPreUpdate(AppBaseEntity appBaseEntity);

    public void onPreRemove(AppBaseEntity appBaseEntity);
}
