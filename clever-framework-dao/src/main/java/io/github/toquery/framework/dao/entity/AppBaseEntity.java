package io.github.toquery.framework.dao.entity;

import io.github.toquery.framework.core.config.AppProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
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
public class AppBaseEntity implements Serializable {

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

    @Column(name = "is_del",updatable = false)
    private boolean isDel;
}
