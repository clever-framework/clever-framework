package io.github.toquery.framework.system.entity;

import io.github.toquery.framework.core.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.persistence.Transient;

/**
 * @author toquery
 * @version 1
 */
//@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@TableName(value = "")
//@Table(name = "sys_config_data")
public class SysConfigData extends BaseEntity {

//    @ManyToOne
//    @JoinColumn(name = "config_id", nullable = false)
    @Transient
    private SysConfig config;

    @Column(name = "name", length = 50)
    private String name;

    @Lob
    @Column(name = "value", columnDefinition = "text")
    private String value;

}
