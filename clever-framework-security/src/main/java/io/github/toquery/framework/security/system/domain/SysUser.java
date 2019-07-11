package io.github.toquery.framework.security.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A user.
 */
@Entity
@Getter
@Setter
@Table(name = "sys_user")
public class SysUser extends AppBaseEntity implements UserDetails {

//    @NotBlank
//    @Length(min = 1, max = 50)
//    @Column(name = "login_name", length = 50, unique = true, nullable = false)
//    private String loginName;

    @NotBlank
    @Size(min = 4, max = 50)
    @Column(name = "user_name", length = 50, unique = true)
    private String username;

    //    @JsonIgnore
    @NotBlank
    @Length(min = 4, max = 100)
    @Column(name = "password", length = 100)
    private String password;

    @Email
    @NotBlank
    @Size(min = 4, max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @NotNull
    @Column(name = "enabled")
    private Boolean enabled = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_password_reset_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastPasswordResetDate = new Date();

    /*
    @JsonIgnoreProperties("users")
//    @JsonBackReference
//    @JsonManagedReference
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "role")
    private Collection<SysUserRole> roles = new HashSet<>();
    */

    /**
     * Spring 用户属性
     */
    @JsonIgnoreProperties(value = {"users", "lastUpdateDatetime", "createDatetime"})
    @ManyToMany
    @JoinTable(
            name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    private Set<SysRole> authorities = new HashSet<>();

    /**
     * 用于前端的角色code
     */
    @Transient
    private Set<String> roles = new HashSet<>();

    public boolean getEnabled() {
        return this.enabled == null ? true : this.enabled;
    }


    /**
     * 将Spring属性转换为角色code
     */
    public void authorities2Roles() {
        if (authorities != null && !authorities.isEmpty()) {
            this.roles = authorities.stream().flatMap(item -> item.getMenus().stream().map(SysMenu::getCode)).collect(Collectors.toSet());
            this.roles.addAll(authorities.stream().map(SysRole::getCode).collect(Collectors.toSet()));
        }
    }

    /**
     * Spring 用户属性

     @JsonIgnoreProperties(value = {"users","lastUpdateDatetime","createDatetime"})
     @Override public Collection<? extends GrantedAuthority> getAuthorities() {
     return this.getRoles();
     }
     */


    /**
     * Spring 用户属性
     */
    @Override
    public boolean isAccountNonExpired() {
        return this.getEnabled();
    }

    /**
     * Spring 用户属性
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.getEnabled();
    }

    /**
     * Spring 用户属性
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return this.getEnabled();
    }

    /**
     * Spring 用户属性
     */
    @Override
    public boolean isEnabled() {
        return getEnabled();
    }
}
