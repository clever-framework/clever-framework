package io.github.toquery.framework.system.entity;

import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Persist AuditEvent managed by the Spring Boot actuator.
 * <p>
 * org.springframework.boot.actuate.audit.AuditEvent
 */
//@Entity
@Getter
@Setter
//@Table(name = "sys_audit_event")
public class PersistentAuditEvent extends AppBaseEntity {

    @NotNull
    @Column(nullable = false)
    private String principal;

    @Column(name = "event_date")
    private Instant auditEventDate;

    @Column(name = "event_type")
    private String auditEventType;

    @ElementCollection
    @MapKeyColumn(name = "name")
    @Column(name = "value")
    @CollectionTable(name = "sys_audit_event_data", joinColumns = @JoinColumn(name = "id"))
    private Map<String, String> data = new HashMap<>();

}
