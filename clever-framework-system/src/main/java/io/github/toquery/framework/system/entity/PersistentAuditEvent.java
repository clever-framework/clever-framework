package io.github.toquery.framework.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.github.toquery.framework.core.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.validation.constraints.NotNull;
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
//@TableName(value = "")
//@Table(name = "sys_audit_event")
public class PersistentAuditEvent extends BaseEntity {

    @NotNull
    @TableField(value = "")
    @Column(nullable = false)
    private String principal;

    @TableField(value = "")
    @Column(name = "event_date")
    private Instant auditEventDate;

    @TableField(value = "")
    @Column(name = "event_type")
    private String auditEventType;

    @ElementCollection
    @MapKeyColumn(name = "name")
    @TableField(value = "")
    @Column(name = "value")
    @CollectionTable(name = "sys_audit_event_data", joinColumns = @JoinColumn(name = "id"))
    private Map<String, String> data = new HashMap<>();

}
