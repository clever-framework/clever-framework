package io.github.toquery.framework.log.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogEntityIgnore;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;

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

    public SysLog(SysLog sysLog, UserDetails sysUser) {
        super.setId(sysLog.getId());
        super.setCreateUserId(sysLog.getCreateUserId());
        super.setUpdateUserId(sysLog.getUpdateUserId());
        super.setCreateDateTime(sysLog.getCreateDateTime());
        super.setUpdateDateTime(sysLog.getUpdateDateTime());

        this.operateDateTime = sysLog.getOperateDateTime();
        this.moduleName = sysLog.getModuleName();
        this.bizName = sysLog.getBizName();
        this.logType = sysLog.getLogType();
        this.rawData = sysLog.getRawData();
        this.targetData = sysLog.getTargetData();

        this.sysUser = sysUser;
    }

    /**
     * 模块名称
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
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
     * 操作时间
     */
    @Column(name = "operate_date_time")
    private LocalDateTime operateDateTime;

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


    @Transient
    private UserDetails sysUser;

}
