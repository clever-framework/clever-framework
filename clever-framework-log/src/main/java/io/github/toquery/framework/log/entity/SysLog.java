package io.github.toquery.framework.log.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.core.log.AppLogType;
//import io.github.toquery.framework.core.log.annotation.AppLogEntityIgnore;
import io.github.toquery.framework.core.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
//@AppLogEntityIgnore
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_log")
@Table(name = "sys_log")
public class SysLog extends BaseEntity {

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
     * 用户ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField(value = "user_id")
    @Column(name = "user_id")
    private Long userId;


    /**
     * 用户名
     */
    @TableField(value = "user_name")
    @Column(name = "user_name")
    private String userName;

    /**
     * 模块名称
     */
    @TableField(value = "module_name")
    @Column(name = "module_name")
    private String moduleName;

    /**
     * 业务名称
     */
    @TableField(value = "biz_name")
    @Column(name = "biz_name")
    private String bizName;


    @Enumerated(EnumType.STRING)
    @TableField(value = "log_type")
    @Column(name = "log_type")
    private AppLogType logType;

    /**
     * 操作时间
     */
    @TableField(value = "operate_date_time")
    @Column(name = "operate_date_time")
    private LocalDateTime operateDateTime;

    /**
     * 原数据
     */
    @Lob
    @TableField(value = "raw_data")
    @Column(columnDefinition = "text", name = "raw_data")
    private String rawData;

    /**
     * 目标数据
     */
    @Lob
    @TableField(value = "target_data")
    @Column(columnDefinition = "text", name = "target_data")
    private String targetData;

    @TableField(exist = false)
    @Transient
    private UserDetails sysUser;

}
