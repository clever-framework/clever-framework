package io.github.toquery.framework.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.toquery.framework.core.domain.AppEntitySort;
import io.github.toquery.framework.core.domain.AppEntityTree;
import io.github.toquery.framework.core.entity.AppBaseEntity;
import io.github.toquery.framework.core.entity.AppEntityLogicDel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

/**
 * A user.
 */
@Entity
@Getter
@Setter
@TableName(value = "sys_area")
@Table(name = "sys_area")
@Where(clause = "deleted = false")
@SQLDelete(sql ="UPDATE sys_area SET deleted = true WHERE id = ?")
public class SysArea extends AppBaseEntity implements AppEntityTree<SysArea>, AppEntitySort, AppEntityLogicDel {

    /**
     * 名称
     */
    @NotBlank
    @Length(min = 4, max = 100)
    @TableField(value = "area_name")
    @Column(name = "area_name", length = 100)
    private String areaName;

    /**
     * 全部名称
     */
    @NotBlank
    @Length(min = 4, max = 255)
    @TableField(value = "full_name")
    @Column(name = "full_name")
    private String fullName;

    /**
     * 编码
     */
    @NotNull
    @Size(max = 50)
    @TableField(value = "area_code")
    @Column(name="area_code",length = 50)
    private String areaCode;

    @TableField(value = "area_level")
    @Column(name = "area_level")
    private int areaLevel = 0;

    /**
     * 上级编码
     */
    @TableField(value = "parent_id")
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 上级编码
     */
    @TableField(value = "parent_code")
    @Column(name = "parent_code")
    private String parentCode;

    /**
     * 上级编码
     */
    @TableField(value = "parent_ids")
    @Column(name = "parent_ids")
    private String parentIds;

    /**
     * 上级编码列表
     */
    @TableField(value = "parent_codes")
    @Column(name = "parent_codes")
    private String parentCodes;


    @TableField(value = "sort_num")
    @Column(name = "sort_num")
    private Integer sortNum = 0;

    @TableField(value = "has_children")
    @Column(name = "has_children")
    private boolean hasChildren = false;

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
    private Collection<SysRole> roles;

    /**
     * 父级信息
     */
    @TableField(exist = false)
    @Transient
    private SysArea parent;

    /**
     * 子集
     */
    @TableField(exist = false)
    @Transient
    private List<SysArea> children;

    @Override
    public int getLevel() {
        return this.areaLevel;
    }

    @Override
    public void setLevel(int areaLevel) {
        this.areaLevel = areaLevel;
    }

    @Override
    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    @Override
    public boolean getHasChildren() {
        return hasChildren;
    }

    @Override
    public int compareTo(SysArea sysArea) {
        return sysArea.getSortNum();
    }

}
