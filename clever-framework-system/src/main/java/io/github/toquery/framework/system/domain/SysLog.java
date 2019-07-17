package io.github.toquery.framework.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.core.annotation.AppLogEntityIgnore;
import io.github.toquery.framework.core.constant.AppLogType;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * app 设计日志，通过 createUserId 获取日志操作人信息ø
 *
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
    @Column(columnDefinition = "text", name = "raw_data")
    private String rawData;

    /**
     * 目标数据
     */
    @Lob
    @Column(columnDefinition = "text", name = "target_data")
    private String targetData;


    /**
     * 日志产生时间
     * 可通过 createDateTime 获取响应时间
     */
    @Deprecated
    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate = new Date();

}
