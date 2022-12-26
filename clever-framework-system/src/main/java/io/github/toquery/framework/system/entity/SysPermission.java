package io.github.toquery.framework.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.toquery.framework.core.entity.AppBaseEntity;
import io.github.toquery.framework.core.entity.AppEntityLogicDel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * A user. 权限
 */
@Entity
@Getter
@Setter
@TableName(value = "sys_permission")
@Table(name = "sys_permission")
@Where(clause = "deleted = false")
@SQLDelete(sql ="UPDATE sys_permission SET deleted = true WHERE id = ?")
public class SysPermission extends AppBaseEntity implements AppEntityLogicDel {

    @TableField(value = "")
    @Column(name = "user_id")
    private Long userId;

    @TableField(value = "")
    @Column(name = "role_id")
    private Long roleId;

    @TableField(value = "")
    @Column(name = "area_id")
    private Long areaId;

    /**
     * 是否删除：1已删除；0未删除
     */
    @TableLogic
    @ColumnDefault("false")
    @TableField(value = "")
    @Column(name = "deleted")
    private Boolean deleted = false;

    @TableField(exist = false)
    @Transient
    private SysUser user;

    @TableField(exist = false)
    @Transient
    private SysRole role;

    @TableField(exist = false)
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
