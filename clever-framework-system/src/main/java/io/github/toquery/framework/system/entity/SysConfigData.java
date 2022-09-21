package io.github.toquery.framework.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.github.toquery.framework.core.log.annotation.AppLogEntity;
import io.github.toquery.framework.core.entity.AppBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.Transient;

/**
 * @author toquery
 * @version 1
 */
//@Entity
@Getter
@Setter
@AppLogEntity
@NoArgsConstructor
@AllArgsConstructor
//@TableName(value = "")
//@Table(name = "sys_config_data")
public class SysConfigData extends AppBaseEntity {

//    @ManyToOne
//    @JoinColumn(name = "config_id", nullable = false)
    @Transient
    private SysConfig config;

    @TableField(value = "")
    @Column(name = "name", length = 50)
    private String name;

    @Lob
    @TableField(value = "")
    @Column(name = "value", columnDefinition = "text")
    private String value;

}
