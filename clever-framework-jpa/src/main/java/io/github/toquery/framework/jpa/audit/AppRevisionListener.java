package io.github.toquery.framework.jpa.audit;

import org.hibernate.envers.RevisionListener;

/**
 * @author toquery
 * @version 1
 */
public class AppRevisionListener implements RevisionListener {


    public void newRevision(Object revisionEntity) {
//        BaseEntity appBaseEntity = (BaseEntity) revisionEntity;
//        appBaseEntity.setRevisionDatetime(22L);
    }
}
