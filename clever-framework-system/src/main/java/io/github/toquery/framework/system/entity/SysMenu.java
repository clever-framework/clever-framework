package io.github.toquery.framework.system.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.core.domain.AppEntitySort;
import io.github.toquery.framework.core.domain.AppEntityTree;
import io.github.toquery.framework.core.entity.AppBaseEntity;
import io.github.toquery.framework.core.entity.AppEntityLogicDel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
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
@Table(name = "sys_menu")
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE sys_menu SET deleted = true WHERE id = ?")
public class SysMenu extends AppBaseEntity implements GrantedAuthority, AppEntityTree<SysMenu>, AppEntitySort, AppEntityLogicDel {

    public SysMenu(Long id, String menuName, String menuCode) {
        this.id = id;
        this.menuName = menuName;
        this.menuCode = menuCode;
    }

    public SysMenu(@NotNull @Size(max = 50) String menuName, @NotNull @Size(max = 50) String menuCode) {
        this.menuName = menuName;
        this.menuCode = menuCode;
    }

    public SysMenu(Long id, String menuName, String menuCode, Long parentId, String parentIds) {
        this.id = id;
        this.menuName = menuName;
        this.menuCode = menuCode;
        this.parentId = parentId;
        this.parentIds = parentIds;
    }

    @NotNull
    @Size(max = 50)
    @Column(name = "menu_name", length = 50)
    private String menuName;

    @NotNull
    @Size(max = 50)
    @Column(name = "menu_code", length = 50)
    private String menuCode;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "menu_status")
    private Integer menuStatus = 1;


    // 树状结构
    @Column(name = "menu_level")
    private int menuLevel = 0;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "parent_ids")
    private String parentIds;

    @Column(name = "tree_path")
    private String treePath;

    @Column(name = "has_children")
    private boolean hasChildren = false;

    @Column(name = "sort_num")
    private Integer sortNum = 0;

    /**
     * 是否删除：1已删除；0未删除
     */

    @ColumnDefault("false")
    @Column(name = "deleted")
    private Boolean deleted = false;

    @Transient
    @TableField(exist = false)
    private Collection<SysRole> roles;

    /**
     * 子集
     */
    @Transient
    @TableField(exist = false)
    private List<SysMenu> children;

    /**
     * 父级信息
     */
    @Transient
    @TableField(exist = false)
    private SysMenu parent;

    public boolean isHasChildren() {
        return hasChildren;
    }

    public boolean getHasChildren() {
        return hasChildren;
    }

    @Override
    public int getLevel() {
        return this.menuLevel;
    }

    @Override
    public void setLevel(int menuLevel) {
        this.menuLevel = menuLevel;
    }

    @Override
    public int compareTo(SysMenu sysMenu) {
        return sysMenu.getSortNum();
    }

    @Override
    public String getAuthority() {
        return this.getMenuCode();
    }
}
