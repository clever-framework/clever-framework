package io.github.toquery.framework.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.entity.AppEntityLogicDel;
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
import java.util.Collection;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Getter
@Setter
@Table(name = "sys_role")
@Where(clause = "deleted = false")
@SQLDelete(sql ="UPDATE SysRole SET deleted = true WHERE id = ?")
public class SysRole extends AppBaseEntity implements AppEntityLogicDel {


    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "role_name", length = 50, unique = true)
    private String roleName;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private boolean deleted = false;

    @Transient
    private Collection<Long> menuIds;

    @Transient
    private Collection<SysUser> users;

    @Transient
    private Collection<SysMenu> menus;

    @Override
    public boolean getDeleted() {
        return deleted;
    }

}
