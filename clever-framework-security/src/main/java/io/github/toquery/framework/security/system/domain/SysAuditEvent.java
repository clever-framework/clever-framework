package io.github.toquery.framework.security.system.domain;

import io.github.toquery.framework.dao.entity.AppBaseEntityPrimaryKeyLong;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Persist AuditEvent managed by the Spring Boot actuator.
 *
 * org.springframework.boot.actuate.audit.AuditEvent
 */
@Getter
@Setter
@Entity
@Table(name = "sys_audit_event")
public class SysAuditEvent extends AppBaseEntityPrimaryKeyLong {


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
    @CollectionTable(name = "sys_audit_event_data", joinColumns = @JoinColumn(name = "event_id"))
    private Map<String, String> data = new HashMap<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SysAuditEvent sysAuditEvent = (SysAuditEvent) o;
        return !(sysAuditEvent.getId() == null || getId() == null) && Objects.equals(getId(), sysAuditEvent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SysAuditEvent{" +
                "principal='" + principal + '\'' +
                ", auditEventDate=" + auditEventDate +
                ", auditEventType='" + auditEventType + '\'' +
                '}';
    }
}
