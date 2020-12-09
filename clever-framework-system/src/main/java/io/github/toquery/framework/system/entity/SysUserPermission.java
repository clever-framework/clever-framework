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

/**
 * A user. 权限
 */
@Entity
@Getter
@Setter
@Table(name = "sys_user_permission")
public class SysUserPermission extends AppBaseEntity implements AppEntitySoftDel {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "area_id")
    private Long areaId;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private boolean deleted = false;

    public boolean getDeleted() {
        return deleted;
    }

    @Transient
    private SysUser user;

    @Transient
    private SysRole role;

    @Transient
    private SysArea area;
}
