package io.github.toquery.framework.system.entity;

import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.entity.AppEntityLogicDel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * A user. 权限
 */
@Entity
@Getter
@Setter
@Table(name = "sys_user_permission")
@Where(clause = "deleted = false")
@SQLDelete(sql ="UPDATE sys_user_permission SET deleted = true WHERE id = ?")
public class SysUserPermission extends AppBaseEntity implements AppEntityLogicDel {

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
    private Boolean deleted = false;

    @Transient
    private SysUser user;

    @Transient
    private SysRole role;

    @Transient
    private SysArea area;


//    @Transient
//    private List<SysUser> users;
//
//    @Transient
//    private List<SysRole> roles;
//
//    @Transient
//    private List<SysArea> areas;
}
