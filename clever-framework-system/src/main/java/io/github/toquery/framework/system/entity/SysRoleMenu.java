package io.github.toquery.framework.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.toquery.framework.core.entity.AppBaseEntity;
import io.github.toquery.framework.core.entity.AppEntityLogicDel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.List;

@Entity
@Getter
@Setter
@TableName(value = "sys_role_menu")
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

    @TableField(value = "role_id")
    @Column(name = "role_id")
    private Long roleId;

    @TableField(value = "menu_id")
    @Column(name = "menu_id")
    private Long menuId;


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
    private SysMenu menu;

    @TableField(exist = false)
    @Transient
    private SysRole role;

    @TableField(exist = false)
    @Transient
    private List<SysMenu> menus;

    @TableField(exist = false)
    @Transient
    private List<SysRole> roles;
}
