package io.github.toquery.framework.system.entity;

import io.github.toquery.framework.core.domain.AppEntitySort;
import io.github.toquery.framework.core.domain.AppEntityTree;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.entity.AppEntityLogicDel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

/**
 * A user.
 */
@Entity
@Getter
@Setter
@Table(name = "sys_area")
@Where(clause = "deleted = false")
@SQLDelete(sql ="UPDATE SysArea SET deleted = true WHERE id = ?")
public class SysArea extends AppBaseEntity implements AppEntityTree<SysArea>, AppEntitySort, AppEntityLogicDel {

    /**
     * 名称
     */
    @NotBlank
    @Length(min = 4, max = 100)
    @Column(name = "area_name", length = 100)
    private String areaName;

    /**
     * 全部名称
     */
    @NotBlank
    @Length(min = 4, max = 255)
    @Column(name = "full_name")
    private String fullName;

    /**
     * 编码
     */
    @NotNull
    @Size(max = 50)
    @Column(length = 50)
    private String areaCode;

    @Column(name = "area_level")
    private int areaLevel = 0;

    /**
     * 上级编码
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 上级编码
     */
    @Column(name = "parent_code")
    private String parentCode;

    /**
     * 上级编码
     */
    @Column(name = "parent_ids")
    private String parentIds;

    /**
     * 上级编码列表
     */
    @Column(name = "parent_codes")
    private String parentCodes;


    @Column(name = "sort_num")
    private Integer sortNum = 0;

    @Column(name = "has_children")
    private boolean hasChildren = false;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private boolean deleted = false;

    @Transient
    private Collection<SysRole> roles;

    /**
     * 父级信息
     */
    @Transient
    private SysArea parent;

    /**
     * 子集
     */
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
    public boolean getDeleted() {
        return deleted;
    }



    @Override
    public int compareTo(SysArea sysArea) {
        return sysArea.getSortNum();
    }

}
