package io.github.toquery.framework.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.github.toquery.framework.core.domain.AppEntitySort;
import io.github.toquery.framework.core.entity.AppBaseEntity;
import io.github.toquery.framework.core.entity.AppEntityLogicDel;
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
import java.util.Collection;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Getter
@Setter
@TableName(value = "sys_role")
@Table(name = "sys_role")
@Where(clause = "deleted = false")
@SQLDelete(sql ="UPDATE sys_role SET deleted = true WHERE id = ?")
public class SysRole extends AppBaseEntity implements AppEntityLogicDel, AppEntitySort {


    @NotBlank
    @Length(min = 2, max = 50)
    @TableField(value = "role_name")
    @Column(name = "role_name", length = 50, unique = true)
    private String roleName;

    @NotNull
    @ColumnDefault("1")
    @TableField(value = "role_status")
    @Column(name = "role_status")
    private Integer roleStatus = 1;

    @TableField(value = "sort_num")
    @Column(name = "sort_num")
    private Integer sortNum = 0;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Collection<Long> menuIds;

    @TableField(exist = false)
    @Transient
    private Collection<SysUser> users;

    @TableField(exist = false)
    @Transient
    private Collection<SysMenu> menus;


}
