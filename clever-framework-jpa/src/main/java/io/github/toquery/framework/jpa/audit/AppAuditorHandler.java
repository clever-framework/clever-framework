package io.github.toquery.framework.jpa.audit;

import io.github.toquery.framework.core.entity.BaseEntity;
import org.springframework.core.Ordered;


/**
 * @author toquery
 * @version 1
 */
public interface AppAuditorHandler extends Ordered {

    boolean enable();

    public void onPrePersist(BaseEntity appBaseEntity);

    public void onPreUpdate(BaseEntity appBaseEntity);

    public void onPreRemove(BaseEntity appBaseEntity);


    void onPostLoad(BaseEntity appBaseEntity);

    void onPostPersist(BaseEntity appBaseEntity);

    void onPostUpdate(BaseEntity appBaseEntity);

    void onPostRemove(BaseEntity appBaseEntity);
}
