package io.github.toquery.framework.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Getter
@Setter
@Table(name = "sys_role")
@JsonIgnoreProperties(value={"hibernateLazyInitializer"})
public class SysRole extends AppBaseEntity {


    @NotBlank
    @Length(min = 2, max = 50)
    @Column(length = 50)
    private String name;

    @JsonIgnoreProperties({"roles", "lastUpdateDatetime", "createDatetime"})
    @ManyToMany // (cascade = CascadeType.ALL)
    @JoinTable(
            name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    private Collection<SysUser> users = new HashSet<>();


    @JsonIgnoreProperties({"roles", "lastUpdateDatetime", "createDatetime"})
//    @ManyToMany(cascade={CascadeType.MERGE,CascadeType.REFRESH})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_role_menu",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    private Collection<SysMenu> menus = new HashSet<>();


    @JsonIgnoreProperties({"roles", "lastUpdateDatetime", "createDatetime"})
    @ManyToOne // (cascade = CascadeType.ALL)
    @JoinTable(
            name = "sys_role_area",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "area_id", referencedColumnName = "id")})
    private SysArea scope = new SysArea();
}
