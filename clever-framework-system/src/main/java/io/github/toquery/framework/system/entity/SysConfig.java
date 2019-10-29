package io.github.toquery.framework.system.entity;

import io.github.toquery.framework.core.annotation.AppLogEntity;
import io.github.toquery.framework.core.annotation.AppLogField;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "config_name", length = 50)
    private String configName;

    @AppLogField("配置排序")
    @Column(name = "sort_num")
    private int sortNum = 0;

    /*
    @ElementCollection
    @MapKeyColumn(name = "name")
    @Column(name = "value")
    @CollectionTable(name = "sys_config_data", joinColumns = @JoinColumn(name = "config_id"))
    private Map<String, String> data = new HashMap<>();
    */

    @OneToMany(mappedBy = "config", cascade = CascadeType.ALL)
    private List<SysConfigData> configMap = new ArrayList<>();
}
