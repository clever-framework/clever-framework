package io.github.toquery.framework.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import io.github.toquery.framework.common.constant.EntityFieldConstant;

import io.github.toquery.framework.core.domain.EntitySort;
import io.github.toquery.framework.core.log.annotation.AppLogField;
import io.github.toquery.framework.core.entity.BaseEntity;
import io.github.toquery.framework.core.entity.EntityLogicDel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author toquery
 * @version 1
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_dict_item")
@SQLRestriction(value = EntityFieldConstant.DOMAIN_FIELD_SOFT_DEL + " = false")
@SQLDelete(sql ="UPDATE sys_dict_item SET deleted = true WHERE id = ?")
public class SysDictItem extends BaseEntity implements EntitySort, EntityLogicDel {


    /**
     * 字典id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "dict_id")
    private Long dictId;

    /**
     * 字典项文本
     */
    @Column(name = "item_text")
    private String itemText;

    /**
     * 字典项值
     */
    @Column(name = "item_value")
    private String itemValue;

    /**
     * 描述
     */
    @Column(name = "item_desc")
    private String itemDesc;

    /**
     * 状态（1禁用 0启用）
     */
    @Column(name = "disable")
    private Integer disable = 0;

    /**
     * 排序
     */
    @AppLogField("配置排序")
    @Column(name = "sort_num")
    private Integer sortNum = 0;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private Boolean deleted = false;

}
