package io.github.toquery.framework.system.entity;

import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.entity.AppEntitySoftDel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Getter
@Setter
@Table(name = "sys_role_menu")
public class SysRoleMenu extends AppBaseEntity implements AppEntitySoftDel {


    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "role_id")
    private Long roleId;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private boolean deleted = false;
    
    @Transient
    private SysMenu menu;

    @Transient
    private SysRole role;

    @Override
    public boolean getDeleted() {
        return deleted;
    }

}
