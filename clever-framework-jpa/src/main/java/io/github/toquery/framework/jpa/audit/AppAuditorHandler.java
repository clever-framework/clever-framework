package io.github.toquery.framework.jpa.audit;

import io.github.toquery.framework.core.entity.AppBaseEntity;
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


    void onPostLoad(AppBaseEntity appBaseEntity);

    void onPostPersist(AppBaseEntity appBaseEntity);

    void onPostUpdate(AppBaseEntity appBaseEntity);

    void onPostRemove(AppBaseEntity appBaseEntity);
}
