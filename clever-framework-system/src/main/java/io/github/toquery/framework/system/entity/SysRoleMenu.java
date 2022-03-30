package io.github.toquery.framework.system.entity;

import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.entity.AppEntityLogicDel;
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

@Entity
@Getter
@Setter
@Table(name = "sys_role_menu")
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted = false")
@SQLDelete(sql ="UPDATE sys_role_menu SET deleted = true WHERE id = ?")
public class SysRoleMenu extends AppBaseEntity implements AppEntityLogicDel {


    public SysRoleMenu(Long roleId, Long menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "menu_id")
    private Long menuId;


    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private Boolean deleted = false;

    @Transient
    private SysMenu menu;

    @Transient
    private SysRole role;

    @Transient
    private List<SysMenu> menus;

    @Transient
    private List<SysRole> roles;
}
