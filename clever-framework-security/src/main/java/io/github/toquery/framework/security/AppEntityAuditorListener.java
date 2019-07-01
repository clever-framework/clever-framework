package io.github.toquery.framework.security;

import io.github.toquery.framework.dao.audit.AppEntityD3Listener;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

/**
 * @author toquery
 * @version 1
 */
@Audited
@MappedSuperclass
@Access(AccessType.FIELD)
//@EntityListeners({AuditingEntityListener.class, AppEntityD3Listener.class})
public interface AppEntityAuditorListener {


}
