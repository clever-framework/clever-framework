package com.toquery.framework.demo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
//@RevisionEntity(AppRevisionListener.class)
//@Table(name = "revinfo")
@AttributeOverrides({
        @AttributeOverride(name = "timestamp", column = @Column(name = "REVTSTMP")),
        @AttributeOverride(name = "id", column = @Column(name = "REV"))})
@Getter
@Setter
public class AuditRevisionEntity extends DefaultRevisionEntity {

    @Column(name = "USERNAME", nullable = false)
    private String username;
}

