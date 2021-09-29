package io.github.toquery.framework.system.entity;

import io.github.toquery.framework.core.domain.AppEntitySort;
import io.github.toquery.framework.core.domain.AppEntityTree;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.entity.AppEntitySoftDel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class SysMenu extends AppBaseEntity implements GrantedAuthority, AppEntityTree<SysMenu>, AppEntitySort, AppEntitySoftDel {

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


    // 树状结构
    @Column(name = "menu_level")
    private int menuLevel = 0;

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
    private boolean deleted = false;

    @Transient
    private Collection<SysRole> roles;

    /**
     * 子集
     */
    @Transient
    private List<SysMenu> children;

    /**
     * 父级信息
     */
    @Transient
    private SysMenu parent;

    public boolean isHasChildren() {
        return hasChildren;
    }

    @Override
    public boolean getDeleted() {
        return deleted;
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
