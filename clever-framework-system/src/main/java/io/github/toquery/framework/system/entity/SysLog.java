package io.github.toquery.framework.system.entity;

import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogEntityIgnore;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

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

}
