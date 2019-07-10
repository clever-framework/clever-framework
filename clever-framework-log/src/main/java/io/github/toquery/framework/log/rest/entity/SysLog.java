package io.github.toquery.framework.log.rest.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.log.annotation.AppLogEntityIgnore;
import io.github.toquery.framework.log.constant.AppLogType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author toquery
 * @version 1
 */
@Entity
@Getter
@Setter
@AppLogEntityIgnore
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_log")
public class SysLog extends AppBaseEntity {


   /* @Id
    @Column
    @RevisionNumber
    @GeneratedValue(generator = "generatedkey")
    @GenericGenerator(name = "generatedkey", strategy = "io.github.toquery.framework.dao.primary.generator.AppJpaEntityLongIDGenerator")
    protected Long id;*/

    @Column(name = "user_id")
    private Long userId;

    /**
     * 模块名称
     */
    @Column(name = "module_name")
    private String moduleName;

    /**
     * 业务名称
     */
    @Column(name = "biz_name")
    private String bizName;


    @Enumerated(EnumType.STRING)
    @Column(name = "log_type")
    private AppLogType logType;

    /**
     * 原数据
     */
    @Lob
    @Column(columnDefinition = "text",name = "raw_data")
    private String rawData;

    /**
     * 目标数据
     */
    @Lob
    @Column(columnDefinition = "text",name = "target_data")
    private String targetData;


    /**
     * 日志产生时间
     */
    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate = new Date();


    @Transient
    private UserDetails user;
}
