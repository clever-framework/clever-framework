package io.github.toquery.framework.system.domain;

import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.system.entity.SysUser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.Audited;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Date;

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
public class AppBaseAuditedEntity extends AppBaseEntity {

    @Transient
    private SysUser createUser;

    @Transient
    private SysUser updateUser;
}
