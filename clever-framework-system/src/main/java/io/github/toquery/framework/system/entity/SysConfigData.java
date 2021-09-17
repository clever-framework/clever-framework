package io.github.toquery.framework.system.entity;

import io.github.toquery.framework.core.log.annotation.AppLogEntity;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "sys_config_data")
public class SysConfigData extends AppBaseEntity {

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
