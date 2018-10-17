package io.github.toquery.framework.dao.entity;

import io.github.toquery.framework.core.config.AppProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author toquery
 * @version 1
 */
@Setter
@Getter
@MappedSuperclass
@Access(AccessType.FIELD)
@EntityListeners(AuditingEntityListener.class)
public class AppBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(name = "create_user_id", length = 32, updatable = false)
    private String createUserId;


    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    @Column(name = "create_time", updatable = false, nullable = false)
    private Date createDatetime;


    @LastModifiedBy
    @Column(name = "last_update_user_id", length = 32)
    private String lastUpdateUserId;


    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update_time", nullable = false)
    @DateTimeFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    private Date lastUpdateDatetime;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = AppProperties.JPA_COLUMN_SOFT_DEL)
    private Boolean isDel = false;


    public boolean getIsDel() {
        return this.isDel == null ? false : this.isDel;
    }

    public void setIsDel(boolean isDel) {
        this.isDel = isDel;
    }
}
