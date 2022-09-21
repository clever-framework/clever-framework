package io.github.toquery.framework.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toquery.framework.common.constant.AppCommonConstant;
import io.github.toquery.framework.core.primary.snowflake.SnowFlake;
import io.github.toquery.framework.core.security.userdetails.AppUserDetails;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.domain.Persistable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

//import org.hibernate.annotations.DynamicUpdate;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Setter
@Getter
//@Audited
@MappedSuperclass
//@DynamicUpdate
//@DynamicInsert
@Access(AccessType.FIELD)
//@EntityListeners({AuditingEntityListener.class, AppEntityD3Listener.class})
//@RevisionEntity(AppRevisionListener.class)
public class AppBaseEntity implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    // @RevisionNumber
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @GeneratedValue(generator = "generatedkey", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "generatedkey", strategy = "io.github.toquery.framework.core.primary.generator.AppSnowFlakeIdGenerator", parameters = {@Parameter(name = "sequence", value = "II_FIRM_DOC_PRM_SEQ")})
    protected Long id;


//    @RevisionTimestamp
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @TableField(value = "")
//    @Column(name = "revision_time", updatable = false, nullable = false)
//    private long revisionDatetime;


    @CreatedBy
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField(value = "create_user_id")
    @Column(name = "create_user_id", length = 32, updatable = false)
    private Long createUserId;


    @CreatedDate
    @CreationTimestamp
    //@Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    // @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    @TableField(value = "create_date_time")
    @Column(name = "create_date_time", updatable = false, nullable = false)
    private LocalDateTime createDateTime;


    @LastModifiedBy
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField(value = "update_user_id")
    @Column(name = "update_user_id", length = 32)
    private Long updateUserId;


    @LastModifiedDate
    @UpdateTimestamp
    //@Temporal(TemporalType.TIMESTAMP)
    @TableField(value = "update_date_time")
    @Column(name = "update_date_time", nullable = false)
    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    // @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    private LocalDateTime updateDateTime;

//    @Version
//    private int version;

    private Long getUserId() {
        Long userId = 0L;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof AppUserDetails) {
            AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();
            userId = appUserDetails.getId();
        }
        return userId;
    }

    public void preInsert(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        this.createUserId = userId;
        this.createDateTime = now;
        this.updateUserId = userId;
        this.updateDateTime = now;
    }

    public void preInsert(Long id, Long userId) {
        this.id = id;
        this.preInsert(userId);
    }

    public void preInsert() {
        Long newId = (this.getId() == null || this.getId() <= 0L) ? new SnowFlake().nextId() : this.getId();
        this.preInsert(newId, this.getUserId());
    }


    public void preUpdate(Long userId) {
        this.updateUserId = userId;
        this.updateDateTime = LocalDateTime.now();
    }

    public void preUpdate() {
        this.preUpdate(this.getUserId());
    }

    @DomainEvents
    public void domainEvents() {
        log.debug("Spring DDD Model: AppBaseEntity ---- @DomainEvents");
    }

    @AfterDomainEventPublication
    public void afterDomainEventPublication() {
        log.debug("Spring DDD Model: AppBaseEntity ---- @AfterDomainEventPublication");
    }

    /**
     * 所有的实体调用save方法都是新的插入，而不是修改
     */
    @JsonIgnore
    @Override
    public boolean isNew() {
        return this.getId() == null || this.getId() == 0L;
    }
}
