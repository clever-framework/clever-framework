package io.github.toquery.framework.security.system.domain;

import io.github.toquery.framework.dao.entity.AppBaseEntityPrimaryKeyLong;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_config")
public class SysConfig extends AppBaseEntityPrimaryKeyLong {

    @Column(name = "biz_id", length = 50)
    private Long bizId;

    @Column(name = "config_group", length = 50)
    private String configGroup;

    @Column(name = "config_name", length = 50)
    private String configName;

    @Column(name = "config_value", length = 500)
    private String configValue;

    @Column(name = "sort_num")
    private int sortNum = 0;
}
