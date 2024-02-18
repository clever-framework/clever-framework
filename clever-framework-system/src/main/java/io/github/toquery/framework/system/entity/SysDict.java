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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import io.github.toquery.framework.common.constant.EntityFieldConstant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
@Table(name = "sys_dict")
@SQLRestriction(value = EntityFieldConstant.DOMAIN_FIELD_SOFT_DEL + " = false")
@SQLDelete(sql ="UPDATE sys_dict SET deleted = true WHERE id = ?")
public class SysDict extends BaseEntity implements EntitySort, EntityLogicDel {

    /**
     * 字典名称
     */
    @AppLogField("字典名称")
    @Column(name = "dict_name", length = 50)
    private String dictName;

    /**
     * 字典编码
     */
    @AppLogField("字典编码")
    @Column(name = "dict_code", unique = true, length = 50)
    private String dictCode;

    /**
     * 描述
     */
    @AppLogField("描述")
    @Column(name = "dict_desc", length = 50)
    private String dictDesc;


    @AppLogField("配置排序")
    @Column(name = "sort_num")
    private Integer sortNum = 0;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private Boolean deleted = false;

    @Transient
    private List<SysDictItem> dictItems;
}
