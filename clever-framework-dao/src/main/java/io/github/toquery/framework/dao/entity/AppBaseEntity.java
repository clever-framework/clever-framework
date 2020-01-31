package io.github.toquery.framework.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.common.constant.AppCommonConstant;
import io.github.toquery.framework.dao.audit.AppEntityD3Listener;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//import org.hibernate.annotations.DynamicUpdate;

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
@EntityListeners({AuditingEntityListener.class, AppEntityD3Listener.class})
//@RevisionEntity(AppRevisionListener.class)
public class AppBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column
    // @RevisionNumber
    @GeneratedValue(generator = "generatedkey")
    @GenericGenerator(name = "generatedkey", strategy = "io.github.toquery.framework.dao.primary.generator.AppSnowFlakeIdGenerator")
    protected Long id;


//    @RevisionTimestamp
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @Column(name = "revision_time", updatable = false, nullable = false)
//    private long revisionDatetime;


    @CreatedBy
    @Column(name = "create_user_id", length = 32, updatable = false)
    private Long createUserId;


    @CreatedDate
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    @Column(name = "create_time", updatable = false, nullable = false)
    private Date createDatetime;


    @LastModifiedBy
    @Column(name = "last_update_user_id", length = 32)
    private Long lastUpdateUserId;


    @LastModifiedDate
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update_time", nullable = false)
    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    private Date lastUpdateDatetime;

//    @Version
//    private int version;


    @DomainEvents
    public void domainEvents() {
        log.debug("Spring DDD Model: AppBaseEntity ---- @DomainEvents");
    }

    @AfterDomainEventPublication
    public void afterDomainEventPublication() {
        log.debug("Spring DDD Model: AppBaseEntity ---- @AfterDomainEventPublication");
    }

}
