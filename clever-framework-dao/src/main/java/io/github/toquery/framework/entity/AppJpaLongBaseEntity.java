package io.github.toquery.framework.entity;

import io.github.toquery.framework.config.AppProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 主键类型为Long的，数据库对象基础类
 */
@Setter
@Getter
@MappedSuperclass
@Access(AccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
public class AppJpaLongBaseEntity extends AppJpaLongIdEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "create_user_id", length = 32, updatable = false)
    private String createUserId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", updatable = false)
    @DateTimeFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    private Date createDatetime;

    @Column(name = "last_update_user_id", length = 32)
    private String lastUpdateUserId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update_time")
    @DateTimeFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    private Date lastUpdateDatetime;

}
