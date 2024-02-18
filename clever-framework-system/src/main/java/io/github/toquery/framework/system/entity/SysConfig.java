package io.github.toquery.framework.system.entity;


import io.github.toquery.framework.core.domain.EntitySort;
import io.github.toquery.framework.core.log.annotation.AppLogField;
import io.github.toquery.framework.core.entity.BaseEntity;
import io.github.toquery.framework.core.entity.EntityLogicDel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

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
public class SysConfig extends BaseEntity implements EntitySort, EntityLogicDel {


    @Column(name = "config_name", unique = true, length = 50)
    private String configName;

    @Lob
    @Column(name = "config_value", columnDefinition = "text")
    private String configValue;

    @Column(name = "config_desc")
    private String configDesc;

    @AppLogField("配置排序")
    @Column(name = "sort_num")
    private Integer sortNum = 0;

    /**
     * 状态（1禁用 0启用）
     */
    @Column(name = "disable")
    private Integer disable = 0;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private Boolean deleted = false;


    /*
    @ElementCollection
    @MapKeyColumn(name = "name")
    @TableField(value = "")
    @Column(name = "value")
    @CollectionTable(name = "sys_config_data", joinColumns = @JoinColumn(name = "config_id"))
    private Map<String, String> data = new HashMap<>();
    */

    // @OneToMany(mappedBy = "config", cascade = CascadeType.ALL)
    @Transient
    private List<SysConfigData> configMap = new ArrayList<>();
}
