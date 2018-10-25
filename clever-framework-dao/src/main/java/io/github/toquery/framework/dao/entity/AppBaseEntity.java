package io.github.toquery.framework.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.core.constant.AppPropertiesDefault;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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
    @Column(name = AppPropertiesDefault.JPA_COLUMN_CREATE_BY, length = 32, updatable = false)
    private String createdBy;


    @CreatedDate
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    //@DateTimeFormat(pattern = AppPropertiesDefault.DATE_TIME_PATTERN)
    @JsonFormat(pattern = AppPropertiesDefault.DATE_TIME_PATTERN, locale = "zh", timezone = "GMT+8")
    @Column(name = AppPropertiesDefault.JPA_COLUMN_CREATE_DATE, updatable = false, nullable = false)
    private Date createdDate;


    @LastModifiedBy
    @Column(name = AppPropertiesDefault.JPA_COLUMN_LAST_MODIFIED_BY, length = 32)
    private String lastModifiedBy;


    @LastModifiedDate
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = AppPropertiesDefault.JPA_COLUMN_LAST_MODIFIED_DATE, nullable = false)
    @JsonFormat(pattern = AppPropertiesDefault.DATE_TIME_PATTERN, locale = "zh", timezone = "GMT+8")
    //@DateTimeFormat(pattern = AppPropertiesDefault.DATE_TIME_PATTERN)
    private Date lastModifiedDate;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = AppPropertiesDefault.JPA_COLUMN_SOFT_DEL)
    private Boolean isDel = false;


    public boolean getIsDel() {
        return this.isDel == null ? false : this.isDel;
    }

    public void setIsDel(boolean isDel) {
        this.isDel = isDel;
    }
}
