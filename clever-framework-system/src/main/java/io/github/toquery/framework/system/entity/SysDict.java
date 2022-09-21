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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@TableName(value = "sys_dict")
@Table(name = "sys_dict")
@Where(clause = "deleted = false")
@SQLDelete(sql ="UPDATE sys_dict SET deleted = true WHERE id = ?")
public class SysDict extends AppBaseEntity implements AppEntitySort, AppEntityLogicDel {

    /**
     * 字典名称
     */
    @AppLogField("字典名称")
    @TableField(value = "dict_name")
    @Column(name = "dict_name", length = 50)
    private String dictName;

    /**
     * 字典编码
     */
    @AppLogField("字典编码")
    @TableField(value = "")
    @Column(name = "dict_code", unique = true, length = 50)
    private String dictCode;

    /**
     * 描述
     */
    @AppLogField("描述")
    @TableField(value = "dict_desc")
    @Column(name = "dict_desc", length = 50)
    private String dictDesc;


    @AppLogField("配置排序")
    @TableField(value = "sort_num")
    @Column(name = "sort_num")
    private Integer sortNum = 0;

    /**
     * 是否删除：1已删除；0未删除
     */
    @TableLogic
    @ColumnDefault("false")
    @TableField(value = "deleted")
    @Column(name = "deleted")
    private Boolean deleted = false;

    @TableField(exist = false)
    @Transient
    private List<SysDictItem> dictItems;
}
