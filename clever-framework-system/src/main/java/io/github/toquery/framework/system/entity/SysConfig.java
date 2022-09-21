package io.github.toquery.framework.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.toquery.framework.core.domain.AppEntitySort;
import io.github.toquery.framework.core.log.annotation.AppLogEntity;
import io.github.toquery.framework.core.log.annotation.AppLogField;
import io.github.toquery.framework.core.entity.AppBaseEntity;
import io.github.toquery.framework.core.entity.AppEntityLogicDel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@TableName(value = "sys_config")
@Table(name = "sys_config")
public class SysConfig extends AppBaseEntity implements AppEntitySort, AppEntityLogicDel {


    @TableField(value = "")
    @Column(name = "config_name", unique = true, length = 50)
    private String configName;

    @Lob
    @TableField(value = "")
    @Column(name = "config_value", columnDefinition = "text")
    private String configValue;

    @TableField(value = "")
    @Column(name = "config_desc")
    private String configDesc;

    @AppLogField("配置排序")
    @TableField(value = "")
    @Column(name = "sort_num")
    private Integer sortNum = 0;

    /**
     * 状态（1禁用 0启用）
     */
    @TableField(value = "")
    @Column(name = "disable")
    private Integer disable = 0;

    /**
     * 是否删除：1已删除；0未删除
     */
    @TableLogic
    @ColumnDefault("false")
    @TableField(value = "")
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
    @TableField(exist = false)
    private List<SysConfigData> configMap = new ArrayList<>();
}
