package io.github.toquery.framework.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Sets;
import io.github.toquery.framework.common.constant.AppCommonConstant;
import io.github.toquery.framework.core.security.userdetails.AppUserDetails;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A user.
 */
@Entity
@Getter
@Setter
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "enabled", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
@Table(name = "sys_user")
public class SysUser extends AppBaseEntity implements UserDetails, AppUserDetails {


    // 用户名，唯一
    @NotBlank
    @Length(min = 4, max = 50)
    @Column(name = "user_name", length = 50, unique = true)
    private String username;

    // 用户昵称
    @NotBlank
    @Length(min = 1, max = 50)
    @Column(name = "nick_name", length = 50, nullable = false)
    private String nickname;

    @JsonIgnore
    @NotBlank
    @Length(min = 4, max = 100)
    @Column(name = "password", length = 100, nullable = false)
    private String password;

    // @NotBlank
    // @Length(min = 11, max = 11)
    @Column(name = "phone", length = 50)
    private String phone;

    //    @Email
//    @NotBlank
//    @Size(min = 4, max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "enabled")
    private Boolean status = true;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_password_reset_date")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN, iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    private Date lastPasswordResetDate = new Date();

    /*
    @JsonIgnoreProperties("users")
//    @JsonBackReference
//    @JsonManagedReference
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "role")
    private Collection<SysUserRole> roles = new HashSet<>();
    */

    @Transient
    private Collection<SysUserPermission> userPermissions;



    @Transient
    private SysRole currentRole;

    @Transient
    private SysArea currentArea;

    @Transient
    private SysUserPermission currentPermission;

    /**
     * 用于前端的角色code
     */
    @Transient
    private Set<String> codes;

    @Transient
    private Set<? extends GrantedAuthority> authorities;


    /**
     * 角色的聚合模式，返回角色所有权限
     */
    public void complexRole() {
        if (userPermissions != null && !userPermissions.isEmpty()) {
            this.authorities = userPermissions.stream().filter(item -> item.getRole() != null).flatMap(item -> item.getRole().getMenus().stream()).collect(Collectors.toSet());
            this.codes = userPermissions.stream().filter(item -> item.getRole() != null).flatMap(item -> item.getRole().getMenus().stream().map(GrantedAuthority::getAuthority)).collect(Collectors.toSet());
        }
    }

    /**
     * 角色的隔离模式，返回当前角色下的权限
     */
    public void isolateRole(Long roleId) {
        if (userPermissions != null && !userPermissions.isEmpty()) {
            Stream<SysUserPermission> sysRoleStream = userPermissions.stream().filter(item -> item.getRole() != null).filter(item -> item.getRole().getMenus() != null && item.getRole().getMenus().size() > 0);
            currentPermission = roleId != null && roleId != 0 ? sysRoleStream.filter(item -> item.getId().equals(roleId)).findAny().get() : sysRoleStream.findFirst().get();
            this.setCurrentPermission(currentPermission);
            this.authorities = Sets.newHashSet(currentRole.getMenus());
            this.codes = currentRole.getMenus().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        }
    }

    public void setCurrentPermission(SysUserPermission currentPermission) {
        this.currentPermission = currentPermission;
        this.currentArea = currentPermission.getArea();
        this.currentRole = currentPermission.getRole();
    }

    /**
     * Spring 用户属性
     */
    @Override
    public boolean isAccountNonExpired() {
        return this.getStatus();
    }

    /**
     * Spring 用户属性
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.getStatus();
    }

    /**
     * Spring 用户属性
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return this.getStatus();
    }

    /**
     * Spring 用户属性
     */
    @Override
    public boolean isEnabled() {
        return this.getStatus();
    }
}
