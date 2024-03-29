package io.github.toquery.framework.system.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.github.toquery.framework.core.domain.EntitySort;
import io.github.toquery.framework.core.entity.BaseEntity;
import io.github.toquery.framework.core.entity.EntityLogicDel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import io.github.toquery.framework.common.constant.EntityFieldConstant;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Getter
@Setter
@Table(name = "sys_role")
@SQLRestriction(value = EntityFieldConstant.DOMAIN_FIELD_SOFT_DEL + " = false")
@SQLDelete(sql ="UPDATE sys_role SET deleted = true WHERE id = ?")
public class SysRole extends BaseEntity implements EntityLogicDel, EntitySort {


    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "role_name", length = 50, unique = true)
    private String roleName;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "role_status")
    private Integer roleStatus = 1;

    @Column(name = "sort_num")
    private Integer sortNum = 0;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
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
