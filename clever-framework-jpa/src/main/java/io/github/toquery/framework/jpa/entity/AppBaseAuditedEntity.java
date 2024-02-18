package io.github.toquery.framework.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.Audited;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Setter
@Getter
@Audited
@MappedSuperclass
//@DynamicUpdate
@Access(AccessType.FIELD)
//@EntityListeners({AuditingEntityListener.class, AppEntityD3Listener.class})
public class AppBaseAuditedEntity extends BaseEntity {

    @Transient
    private UserDetails createUser;

    @Transient
    private UserDetails updateUser;
}
