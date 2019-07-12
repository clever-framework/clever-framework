package io.github.toquery.framework.system.domain;

import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.core.annotation.AppLogEntity;
import io.github.toquery.framework.core.annotation.AppLogField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author toquery
 * @version 1
 */
@Entity
@Getter
@Setter
@AppLogEntity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_config")
public class SysConfig extends AppBaseEntity {

    @AppLogField("业务ID")
    @Column(name = "biz_id", length = 50)
    private Long bizId;
    @AppLogField("配置组名称")
    @Column(name = "config_group", length = 50)
    private String configGroup;
    @AppLogField("配置名称")
    @Column(name = "config_name", length = 50)
    private String configName;
    @AppLogField("配置值")
    @Column(name = "config_value", length = 500)
    private String configValue;
    @AppLogField("配置排序")
    @Column(name = "sort_num")
    private int sortNum = 0;
}
