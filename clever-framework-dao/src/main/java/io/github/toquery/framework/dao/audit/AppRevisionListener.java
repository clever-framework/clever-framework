package io.github.toquery.framework.dao.audit;

import io.github.toquery.framework.dao.entity.AppBaseEntity;
import org.hibernate.envers.RevisionListener;

/**
 * @author toquery
 * @version 1
 */
public class AppRevisionListener implements RevisionListener {


    public void newRevision(Object revisionEntity) {
        AppBaseEntity appBaseEntity = (AppBaseEntity)revisionEntity;
    }
}
